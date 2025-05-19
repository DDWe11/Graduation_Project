package org.jevonD.wastewaterMS.modules.data.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.jevonD.wastewaterMS.modules.data.dto.request.SensorDataCollectReq;
import org.jevonD.wastewaterMS.modules.data.dto.response.SensorDataCollectResp;
import org.jevonD.wastewaterMS.modules.data.dto.websocket.CollectParamsReq;
import org.jevonD.wastewaterMS.modules.data.service.CollectFrequencyConfig;
import org.jevonD.wastewaterMS.modules.data.service.SensorDataCollectService;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorDevice;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@ServerEndpoint("/ws/data/collect")
@Component
public class SensorDataWebSocketEndpoint {

    private static final Logger log = LoggerFactory.getLogger(SensorDataWebSocketEndpoint.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static final Map<String, Object> sessionSendLock = new ConcurrentHashMap<>();

    private static SensorDataCollectService sensorDataCollectService;
    private static SensorDeviceRepository sensorDeviceRepository;

    @Resource
    public void setSensorDataCollectService(SensorDataCollectService service) {
        sensorDataCollectService = service;
    }

    @Resource
    public void setSensorDeviceRepository(SensorDeviceRepository repo) {
        sensorDeviceRepository = repo;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("[WebSocket] 新连接：{}", session.getId());
        WebSocketSessionManager.addSession(session);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("[WebSocket] 断开连接：{}", session.getId());
        WebSocketSessionManager.removeSession(session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("[WebSocket] 收到客户端消息: {}", message);
        try {
            CollectParamsReq req = objectMapper.readValue(message, CollectParamsReq.class);
            List<Long> sensorIds = req.getSensorIds();
            String action = req.getAction() != null ? req.getAction().toLowerCase() : "start";
            Integer userFreq = req.getFreq();

            // 1. 参数校验
            if (userFreq != null && userFreq <= 0) {
                session.getBasicRemote().sendText("{\"error\":\"采集频率必须大于0\"}");
                log.warn("[WebSocket] 参数校验失败：无效频率 {}", userFreq);
                return;
            }

            List<SensorDevice> devices;
            if (sensorIds == null || sensorIds.isEmpty()) {
                // 启动全部设备采集
                devices = sensorDeviceRepository.findEnabledAndActiveDevices();
                sensorIds = new ArrayList<>();
                for (SensorDevice device : devices) sensorIds.add(device.getId());
            } else {
                devices = sensorDeviceRepository.selectBatchIds(sensorIds);
                // 校验：请求的ID和查到的设备数量不一致
                if (devices.isEmpty() || devices.size() != sensorIds.size()) {
                    session.getBasicRemote().sendText("{\"error\": \"无效的设备ID，请检查设备列表！\"}");
                    log.warn("[WebSocket] 参数校验失败：无效的设备ID {}", sensorIds);
                    return;
                }
            }

            // 停止采集
            if ("stop".equals(action)) {
                if (sensorIds == null || sensorIds.isEmpty()) {
                    Map<Long, ScheduledFuture<?>> taskMap = WebSocketSessionManager.getAllTasks(session.getId());
                    if (taskMap != null && !taskMap.isEmpty()) {
                        taskMap.values().forEach(task -> { if (task != null) task.cancel(true); });
                        WebSocketSessionManager.removeSession(session.getId());
                        log.info("[WebSocket] 已停止全部采集任务，session: {}", session.getId());
                    }
                    session.getBasicRemote().sendText("{\"status\":\"stopped_all\"}");
                } else {
                    for (Long sensorId : sensorIds) {
                        WebSocketSessionManager.removeTask(session.getId(), sensorId);
                    }
                    log.info("[WebSocket] 已停止部分采集任务: session={}, sensorIds={}", session.getId(), sensorIds);
                    session.getBasicRemote().sendText("{\"status\":\"stopped_partial\",\"sensorIds\":" + objectMapper.writeValueAsString(sensorIds) + "}");
                }
                return;
            }

            // 用于日志展示的设备名Map
            Map<Long, String> deviceNameMap = new HashMap<>();
            for (SensorDevice device : devices) {
                deviceNameMap.put(device.getId(), device.getName());
            }

            for (SensorDevice device : devices) {
                Long deviceId = device.getId();
                String code = device.getCode();
                String typeCode = parseTypeCode(code);

                int freq;
                if (sensorIds.size() == 1 && userFreq != null) {
                    int minFreq = CollectFrequencyConfig.getMinFreq(typeCode);
                    freq = Math.max(userFreq, minFreq);
                } else {
                    freq = CollectFrequencyConfig.getDefaultFreq(typeCode);
                }

                // 关闭已有任务（防止重复）
                WebSocketSessionManager.removeTask(session.getId(), deviceId);

                // 定时采集推送，去掉进度条，只打印采集日志
                ScheduledFuture<?> future = WebSocketSessionManager.getScheduler().scheduleAtFixedRate(() -> {
                    try {
                        SensorDataCollectReq collectReq = new SensorDataCollectReq();
                        collectReq.setSensorIds(List.of(deviceId));
                        List<SensorDataCollectResp> respList = sensorDataCollectService.collectAndStoreSensorData(collectReq);

                        // 日志输出每条数据
                        for (SensorDataCollectResp resp : respList) {
                            log.info("[采集推送] {} [{}] 时间: {} 数值: {} {}",
                                    deviceNameMap.get(resp.getSensorId()),
                                    resp.getSensorId(),
                                    resp.getTimestamp(),
                                    resp.getValue(),
                                    resp.getUnit()
                            );
                        }

                        // WebSocket 推送数据
                        Object lock = sessionSendLock.computeIfAbsent(session.getId(), k -> new Object());
                        synchronized (lock) {
                            session.getBasicRemote().sendText(objectMapper.writeValueAsString(respList));
                        }
                    } catch (Exception e) {
                        log.error("[WebSocket] 采集/推送异常", e);
                    }
                }, 0, freq, TimeUnit.SECONDS);

                WebSocketSessionManager.addTask(session.getId(), deviceId, future);
                log.info("[WebSocket] 启动采集：sessionId={}, deviceId={}, freq={}s", session.getId(), deviceId, freq);
            }

            session.getBasicRemote().sendText("{\"status\":\"started\"}");

        } catch (Exception e) {
            log.error("[WebSocket] 消息处理异常", e);
            try {
                session.getBasicRemote().sendText("{\"error\":\"参数错误或服务端异常\"}");
            } catch (IOException ignored) {}
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        log.error("[WebSocket] 发生异常: session={}, error={}", session != null ? session.getId() : null, thr.getMessage(), thr);
        if (session != null) {
            WebSocketSessionManager.removeSession(session.getId());
        }
    }

    /**
     * 从设备 code 提取 typeCode，如 TEMP250515001 -> TEMP
     */
    private static String parseTypeCode(String code) {
        if (code == null) return "";
        int i = 0;
        while (i < code.length() && Character.isUpperCase(code.charAt(i))) i++;
        return code.substring(0, i);
    }
}