package org.jevonD.wastewaterMS.modules.data.websocket;

import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * WebSocket 会话与定时采集任务统一管理器
 * 支持多客户端连接、每个 session 多个传感器采集任务，频率独立、线程安全
 */
public class WebSocketSessionManager {

    private static final Logger log = LoggerFactory.getLogger(WebSocketSessionManager.class);

    // 所有在线 WebSocket 连接 (key: sessionId)
    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
    // 每个 session 可有多个采集任务 (key: sessionId, value: {sensorId: 定时任务})
    private static final Map<String, Map<Long, ScheduledFuture<?>>> SESSION_TASKS = new ConcurrentHashMap<>();
    // 全局定时任务线程池
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    runnable -> {
                        Thread t = new Thread(runnable);
                        t.setDaemon(true);
                        t.setName("ws-collect-scheduler-" + t.getId());
                        return t;
                    }
            );

    /**
     * 注册一个新的 WebSocket Session，并初始化任务池
     */
    public static void addSession(Session session) {
        SESSION_MAP.put(session.getId(), session);
        SESSION_TASKS.put(session.getId(), new ConcurrentHashMap<>());
        log.info("[WebSocketManager] 新增 Session: {}", session.getId());
    }

    /**
     * 注销 Session 及其所有采集任务
     */
    public static void removeSession(String sessionId) {
        SESSION_MAP.remove(sessionId);
        Map<Long, ScheduledFuture<?>> taskMap = SESSION_TASKS.remove(sessionId);
        if (taskMap != null) {
            taskMap.values().forEach(task -> {
                if (task != null) task.cancel(true);
            });
            log.info("[WebSocketManager] 注销 Session: {}，定时任务全部取消", sessionId);
        }
    }

    /**
     * 获取指定 sessionId 的 Session
     */
    public static Session getSession(String sessionId) {
        return SESSION_MAP.get(sessionId);
    }

    /**
     * 获取线程池（给 WebSocket 端点调度用）
     */
    public static ScheduledExecutorService getScheduler() {
        return SCHEDULER;
    }

    /**
     * 注册（或覆盖）session 下某 sensorId 的定时采集任务
     */
    public static void addTask(String sessionId, Long sensorId, ScheduledFuture<?> future) {
        SESSION_TASKS.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>()).put(sensorId, future);
        log.info("[WebSocketManager] 注册定时采集任务: sessionId={}, sensorId={}", sessionId, sensorId);
    }

    /**
     * 获取当前 session 下所有采集任务
     */
    public static Map<Long, ScheduledFuture<?>> getAllTasks(String sessionId) {
        return SESSION_TASKS.getOrDefault(sessionId, Collections.emptyMap());
    }

    /**
     * 获取当前 session 下某个 sensorId 的定时任务
     */
    public static ScheduledFuture<?> getTask(String sessionId, Long sensorId) {
        Map<Long, ScheduledFuture<?>> taskMap = SESSION_TASKS.get(sessionId);
        return (taskMap != null) ? taskMap.get(sensorId) : null;
    }

    /**
     * 移除并关闭指定 sessionId 下 sensorId 的定时任务
     */
    public static void removeTask(String sessionId, Long sensorId) {
        Map<Long, ScheduledFuture<?>> taskMap = SESSION_TASKS.get(sessionId);
        if (taskMap != null && taskMap.containsKey(sensorId)) {
            ScheduledFuture<?> future = taskMap.remove(sensorId);
            if (future != null) future.cancel(true);
            log.info("[WebSocketManager] 取消采集任务: sessionId={}, sensorId={}", sessionId, sensorId);
        }
    }

    /**
     * (可选) 一次性停止所有 session、所有任务（应用关闭或全局下线场景）
     */
    public static void shutdownAll() {
        SESSION_MAP.clear();
        SESSION_TASKS.values().forEach(map -> map.values().forEach(future -> {
            if (future != null) future.cancel(true);
        }));
        SESSION_TASKS.clear();
        SCHEDULER.shutdown();
        log.info("[WebSocketManager] 全部会话与任务已清理，线程池关闭。");
    }

    /**
     * (可选) 获取所有在线 SessionId（用于监控/调试）
     */
    public static Iterable<String> getAllSessionIds() {
        return SESSION_MAP.keySet();
    }
}