package org.jevonD.wastewaterMS.modules.data.service.impl;

import org.jevonD.wastewaterMS.common.utils.SensorSimulateUtils;
import org.jevonD.wastewaterMS.modules.data.dto.request.SensorDataCollectReq;
import org.jevonD.wastewaterMS.modules.data.dto.response.SensorDataCollectResp;
import org.jevonD.wastewaterMS.modules.data.entity.SensorData;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorDevice;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorType;
import org.jevonD.wastewaterMS.modules.data.repository.SensorDataRepository;
import org.jevonD.wastewaterMS.modules.data.service.SensorDataCollectService;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorDeviceRepository;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorTypeRepository;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorProcessBindRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensorDataCollectServiceImpl implements SensorDataCollectService {

    @Autowired
    private SensorDeviceRepository sensorDeviceRepository;
    @Autowired
    private SensorTypeRepository sensorTypeRepository;
    @Autowired
    private SensorProcessBindRepository sensorProcessBindRepository;
    @Autowired
    private SensorDataRepository sensorDataRepository;

    private static final Logger log = LoggerFactory.getLogger(SensorDataCollectServiceImpl.class);

    @Override
    public List<SensorDataCollectResp> collectAndStoreSensorData(SensorDataCollectReq req) {
        // 1. 查询设备列表（带类型字段），只查启用+在线/异常
        List<Integer> deviceStates = Arrays.asList(1, 2); // 在线、异常
        List<SensorDevice> deviceList = sensorDeviceRepository.findByIdsAndCodes(
                req.getSensorIds(), req.getCode(), 1, deviceStates);

        if (deviceList.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 查找所有typeId对应的类型信息，避免多次查
        Set<Long> typeIds = deviceList.stream().map(SensorDevice::getTypeId).collect(Collectors.toSet());
        Map<Long, SensorType> typeMap = sensorTypeRepository.findByIds(typeIds)
                .stream().collect(Collectors.toMap(SensorType::getId, t -> t));

        // 3. 查找与设备相关的工艺绑定关系
        Map<Long, String> sensorIdToOutletType = new HashMap<>();
        List<Long> sensorIdList = deviceList.stream().map(SensorDevice::getId).collect(Collectors.toList());
        // 出水口、进水口的绑定类型可根据实际业务分流补充
        List<Long> outletSensorIds = sensorProcessBindRepository.findOutletSensorIds();
        List<Long> inletSensorIds = sensorProcessBindRepository.findInletSensorIds();

//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
//        System.out.println("【请求时区】" + TimeZone.getDefault().getID());
        LocalDateTime now = LocalDateTime.now();
//        System.out.println("【接口采集方法】now: " + now);

        List<SensorDataCollectResp> respList = new ArrayList<>();
        List<SensorData> saveList = new ArrayList<>();

        for (SensorDevice device : deviceList) {
            Long deviceId = device.getId();
            Long typeId = device.getTypeId();
            SensorType type = typeMap.get(typeId);
            String typeCode = type.getCode(); // 大写，如 FLOW、TURBIDITY
            String name = device.getName();

            Double value;
            switch (typeCode) {
                case "FLOW":
                    if (inletSensorIds.contains(deviceId)) {
                        value = SensorSimulateUtils.generateFlowIn(now);
                    } else if (outletSensorIds.contains(deviceId)) {
                        value = SensorSimulateUtils.generateFlowOut(now);
                    } else {
                        value = SensorSimulateUtils.generateFlow(now);
                    }
                    break;
                case "TURBIDITY":
                    if (inletSensorIds.contains(deviceId)) {
                        value = SensorSimulateUtils.generateTurbidityIn(now);
                    } else if (outletSensorIds.contains(deviceId)) {
                        value = SensorSimulateUtils.generateTurbidityOut(now);
                    } else {
                        value = SensorSimulateUtils.generateTurbidity(now);
                    }
                    break;
                case "TEMP":
                    value = SensorSimulateUtils.generateTemperature(now);
                    break;
                case "HUMIDITY":
                    value = SensorSimulateUtils.generateHumidity(now);
                    break;
                case "POWER":
                    value = SensorSimulateUtils.generatePower(now);
                    break;
                default:
                    continue;
            }

            // 单位优先用设备自定义，否则用类型默认
            String unit = (device.getUnit() != null && !device.getUnit().isEmpty())
                    ? device.getUnit() : type.getDefaultUnit();

            // 组装返回DTO
            SensorDataCollectResp resp = new SensorDataCollectResp();
            resp.setSensorId(deviceId);
            resp.setTypeId(typeId);
            resp.setName(name);
            resp.setValue(value);
            resp.setUnit(unit);
            resp.setTimestamp(now);
            respList.add(resp);

            // 组装数据库实体
            SensorData entity = new SensorData();
            entity.setSensorId(deviceId);
            entity.setValue(value);
            entity.setUnit(unit);
            entity.setTimestamp(now);
            entity.setRemark(null);
            entity.setAlarmFlag(0); // 采集时不报警，后续有规则再补
            saveList.add(entity);
        }

        // 批量写入历史数据表
        if (!saveList.isEmpty()) {
            log.info("【采集生成的数据】");
            for (SensorData data : saveList) {
                log.info(data.toString());
            }sensorDataRepository.insertBatch(saveList);
        }
        return respList;
    }
}