package org.jevonD.wastewaterMS.modules.data.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.request.SensorHistoryDataQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.response.SensorHistoryDataQueryResp;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.response.SensorHistoryDataQueryResp.SensorHistoryDataItem;
import org.jevonD.wastewaterMS.modules.data.entity.SensorData;
import org.jevonD.wastewaterMS.modules.data.service.SensorHistoryDataQueryService;
import org.jevonD.wastewaterMS.modules.data.repository.SensorDataRepository;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorDevice;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorType;
import org.jevonD.wastewaterMS.modules.sensor.entity.enums.DeviceState;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorDeviceRepository;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorTypeRepository;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorProcessBindRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 传感器历史数据分页与多条件查询实现
 */
@Service
public class SensorHistoryDataQueryServiceImpl implements SensorHistoryDataQueryService {

    @Autowired
    private SensorDataRepository sensorDataRepository;
    @Autowired
    private SensorDeviceRepository sensorDeviceRepository;
    @Autowired
    private SensorTypeRepository sensorTypeRepository;
    @Autowired
    private SensorProcessBindRepository sensorProcessBindRepository;

    @Override
    public SensorHistoryDataQueryResp querySensorHistoryData(SensorHistoryDataQueryReq req) {
        // 1. 初步 sensorIds 集合
        List<Long> baseSensorIds = null;
        if (req.getProcessUnitId() != null) {
            baseSensorIds = sensorProcessBindRepository.findSensorIdsByProcessUnitId(req.getProcessUnitId());
            if (baseSensorIds.isEmpty()) return emptyPage();
        } else if (Boolean.TRUE.equals(req.getGlobalOnly())) {
            baseSensorIds = sensorDeviceRepository.findUnboundSensorIds();
            if (baseSensorIds.isEmpty()) return emptyPage();
        } else if (req.getSensorIds() != null && !req.getSensorIds().isEmpty()) {
            baseSensorIds = req.getSensorIds();
        }
        // baseSensorIds == null 表示“全设备”

        // 2. 二级筛选（设备类型、状态等），只查device表
        List<Long> finalSensorIds;
        boolean hasType = req.getSensorTypeCode() != null && !req.getSensorTypeCode().isEmpty();
        boolean hasState = req.getDeviceState() != null;
        if (hasType || hasState) {
            List<SensorDevice> devices = sensorDeviceRepository.findByIdsAndTypeAndState(
                    baseSensorIds, // 只在baseSensorIds里查（null时查全表）
                    req.getSensorTypeCode(),
                    req.getDeviceState()
            );
            if (devices.isEmpty()) return emptyPage();
            finalSensorIds = devices.stream().map(SensorDevice::getId).toList();
        } else if (baseSensorIds != null) {
            finalSensorIds = baseSensorIds;
        } else {
            finalSensorIds = null; // 全部设备
        }

        // 3. 主表数据分页查找（传感器ID/报警/时间）
        Page<SensorData> page = new Page<>(req.getPage(), req.getSize());
        List<SensorData> dataList = sensorDataRepository.findPageByConditions(
                page,
                finalSensorIds,                  // null为全表
                req.getAlarmFlag(),
                req.getStartTime(),
                req.getEndTime()
        );

        // 4. 一次查完所有需要的设备/类型信息（避免N+1）
        Set<Long> sensorIdSet = dataList.stream().map(SensorData::getSensorId).collect(Collectors.toSet());
        Map<Long, SensorDevice> deviceMap = sensorIdSet.isEmpty()
                ? Collections.emptyMap()
                : sensorDeviceRepository.findDevicesByIds(sensorIdSet).stream()
                .collect(Collectors.toMap(SensorDevice::getId, d -> d));
        Set<Long> typeIdSet = deviceMap.values().stream().map(SensorDevice::getTypeId).collect(Collectors.toSet());
        Map<Long, SensorType> typeMap = typeIdSet.isEmpty()
                ? Collections.emptyMap()
                : sensorTypeRepository.findByIds(typeIdSet).stream()
                .collect(Collectors.toMap(SensorType::getId, t -> t));

        // 5. 拼装 resp
        List<SensorHistoryDataItem> records = new ArrayList<>();
        int baseIdx = (req.getPage() - 1) * req.getSize() + 1;
        for (int i = 0; i < dataList.size(); i++) {
            SensorData data = dataList.get(i);
            SensorDevice device = deviceMap.get(data.getSensorId());
            SensorType type = (device != null) ? typeMap.get(device.getTypeId()) : null;

            SensorHistoryDataItem item = new SensorHistoryDataItem();
            item.setId((long) (baseIdx + i));
            item.setSensorId(data.getSensorId());
            item.setSensorTypeCode(type != null ? type.getCode() : null);
            item.setSensorTypeName(type != null ? type.getName() : null);
            item.setValue(data.getValue());
            item.setUnit(data.getUnit());
            item.setTimestamp(data.getTimestamp());
            DeviceState ds = device != null ? device.getDeviceState() : null;
            item.setDeviceState(ds != null ? ds.getValue() : null);
            item.setAlarmFlag(data.getAlarmFlag());
            item.setRemark(data.getRemark());
            records.add(item);
        }
        SensorHistoryDataQueryResp resp = new SensorHistoryDataQueryResp();
        resp.setTotal(page.getTotal());
        resp.setRecords(records);
        return resp;
    }

    private SensorHistoryDataQueryResp emptyPage() {
        SensorHistoryDataQueryResp resp = new SensorHistoryDataQueryResp();
        resp.setTotal(0L);
        resp.setRecords(Collections.emptyList());
        return resp;
    }
}