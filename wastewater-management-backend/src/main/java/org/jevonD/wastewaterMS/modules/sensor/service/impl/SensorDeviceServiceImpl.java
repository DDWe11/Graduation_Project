package org.jevonD.wastewaterMS.modules.sensor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorDeviceReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorDeviceResp;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorDevice;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorType;
import org.jevonD.wastewaterMS.modules.sensor.entity.enums.DeviceState;
import org.jevonD.wastewaterMS.modules.sensor.entity.enums.SensorStatus;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorDeviceRepository;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorProcessBindRepository;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorTypeRepository;
import org.jevonD.wastewaterMS.modules.sensor.service.SensorDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorDeviceServiceImpl extends ServiceImpl<SensorDeviceRepository, SensorDevice> implements SensorDeviceService {

    @Autowired
    private final SensorTypeRepository sensorTypeRepository;

    @Autowired
    private final SensorDeviceRepository sensorDeviceRepository;

    @Autowired
    private final SensorProcessBindRepository sensorProcessBindRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorDeviceResp.DeviceInfo createDevice(SensorDeviceReq.CreateSensor req) {

        // 查找传感器类型
        SensorType type = sensorTypeRepository.selectById(req.getTypeId());
        if (type == null) {
            throw new IllegalArgumentException("传感器类型不存在");
        }

        // 生成设备ID
//        Long newId = sensorDeviceRepository.generateNextId();

        // 生成唯一编码：如 FLOW240515001
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int sequence = sensorDeviceRepository.countByTypeToday(type.getId(), datePrefix) + 1;
        String code = type.getCode() + datePrefix + String.format("%03d", sequence);

        // 构建设备
        SensorDevice device = new SensorDevice();
//        device.setId(newId);
        device.setCode(code);
        device.setName(type.getName());
        device.setTypeId(type.getId());
        device.setUnit(type.getDefaultUnit());
        // 状态处理：默认启用
        if (req.getStatus() == null) {
            device.setStatus(SensorStatus.ENABLED);
        } else {
            device.setStatus(SensorStatus.fromValue(req.getStatus()));
        }
        device.setRemark(req.getRemark());
        device.setCreateTime(LocalDateTime.now());

        sensorDeviceRepository.insert(device);

        // 构造返回
        SensorDeviceResp.DeviceInfo resp = new SensorDeviceResp.DeviceInfo();
        resp.setId(device.getId());
        resp.setCode(device.getCode());
        resp.setName(device.getName());
        resp.setTypeId(type.getId());
        resp.setUnit(device.getUnit());
        resp.setStatus(device.getStatus().getValue()); // 返回整数
        resp.setRemark(device.getRemark());
        resp.setCreateTime(device.getCreateTime());

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorDeviceResp.DeviceInfo updateDevice(SensorDeviceReq.UpdateSensorInfo req) {
        // 查找传感器设备
        SensorDevice device = sensorDeviceRepository.selectById(req.getId());
        if (device == null) {
            throw new IllegalArgumentException("传感器设备不存在");
        }

        // 查找对应的传感器类型
        SensorType type = sensorTypeRepository.selectById(device.getTypeId());
        if (type == null) {
            throw new IllegalStateException("设备所关联的传感器类型不存在");
        }

        // 只允许更新 remark
        if (req.getRemark() != null) {
            device.setRemark(req.getRemark());
            sensorDeviceRepository.updateById(device);  // 仅执行更新
        }

        // 构造返回对象
        SensorDeviceResp.DeviceInfo resp = new SensorDeviceResp.DeviceInfo();
        resp.setId(device.getId());
        resp.setCode(device.getCode());
        resp.setName(device.getName());
        resp.setTypeId(type.getId());
        resp.setUnit(device.getUnit());
        resp.setStatus(device.getStatus().getValue());
        resp.setRemark(device.getRemark());
        resp.setCreateTime(device.getCreateTime());

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorDeviceResp.DeleteInfo deleteDevice(SensorDeviceReq.DeleteSensor req) {
        // 1. 查询设备是否存在
        SensorDevice device = sensorDeviceRepository.selectById(req.getId());
        if (device == null) {
            throw new IllegalArgumentException("传感器设备不存在，无法删除");
        }

        // 2. 检查是否存在绑定关系
        boolean isBound = sensorProcessBindRepository.existsBindWithSensorId(req.getId());
        if (isBound) {
            throw new IllegalArgumentException("该传感器已绑定至工艺流程，无法删除");
        }

        // 3. 执行删除
        sensorDeviceRepository.deleteById(req.getId());

        // 4. 构造响应
        SensorDeviceResp.DeleteInfo resp = new SensorDeviceResp.DeleteInfo();
        resp.setId(device.getId());
        resp.setCode(device.getCode());
        resp.setName(device.getName());
        resp.setDeletedTime(LocalDateTime.now());

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorDeviceResp.DeviceInfo changeStatus(SensorDeviceReq.ChangeSensorStatus req) {
        // 1. 查找传感器设备
        SensorDevice device = sensorDeviceRepository.selectById(req.getId());
        if (device == null) {
            throw new IllegalArgumentException("传感器设备不存在");
        }

        // 2. 校验状态合法性
        SensorStatus newStatus = SensorStatus.fromValue(req.getStatus());
        if (newStatus == null) {
            throw new IllegalArgumentException("无效的启用状态（应为 0 或 1）");
        }

        // 3. 设置并更新状态
        device.setStatus(newStatus);
        sensorDeviceRepository.updateById(device);

        // 4. 查询类型信息（用于响应构建）
        SensorType type = sensorTypeRepository.selectById(device.getTypeId());
        if (type == null) {
            throw new IllegalStateException("传感器类型信息缺失");
        }

        // 5. 构造返回对象
        SensorDeviceResp.DeviceInfo resp = new SensorDeviceResp.DeviceInfo();
        resp.setId(device.getId());
        resp.setCode(device.getCode());
        resp.setName(device.getName());
        resp.setTypeId(type.getId());
        resp.setUnit(device.getUnit());
        resp.setStatus(device.getStatus().getValue());
        resp.setRemark(device.getRemark());
        resp.setCreateTime(device.getCreateTime());

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorDeviceResp.DeviceInfo changeDeviceState(SensorDeviceReq.ChangeDeviceState req) {
        // 1. 查找传感器设备
        SensorDevice device = sensorDeviceRepository.selectById(req.getId());
        if (device == null) {
            throw new IllegalArgumentException("传感器设备不存在");
        }

        // 2. 校验实时状态合法性
        DeviceState newState = DeviceState.fromValue(req.getDeviceState());
        if (newState == null) {
            throw new IllegalArgumentException("无效的设备状态（应为 0=离线，1=在线，2=异常）");
        }

        // 3. 设置并更新实时状态
        device.setDeviceState(newState);
        sensorDeviceRepository.updateById(device);

        // 4. 查询类型信息（用于响应构建）
        SensorType type = sensorTypeRepository.selectById(device.getTypeId());
        if (type == null) {
            throw new IllegalStateException("传感器类型信息缺失");
        }

        // 5. 构造返回对象
        SensorDeviceResp.DeviceInfo resp = new SensorDeviceResp.DeviceInfo();
        resp.setId(device.getId());
        resp.setCode(device.getCode());
        resp.setName(device.getName());
        resp.setTypeId(type.getId());
        resp.setUnit(device.getUnit());
        resp.setStatus(device.getStatus().getValue());
        resp.setDeviceState(device.getDeviceState().getValue());
        resp.setRemark(device.getRemark());
        resp.setCreateTime(device.getCreateTime());

        return resp;
    }


    @Override
    public SensorDeviceResp.QueryList listDevices(SensorDeviceReq.QuerySensorDevices req) {
        List<SensorDevice> devices = sensorDeviceRepository.findByConditions(req);

        List<SensorDeviceResp.DeviceInfo> deviceInfos = devices.stream().map(device -> {
            SensorType type = sensorTypeRepository.selectById(device.getTypeId());
            SensorDeviceResp.DeviceInfo info = new SensorDeviceResp.DeviceInfo();
            info.setId(device.getId());
            info.setCode(device.getCode());
            info.setName(device.getName());
            info.setTypeId(device.getTypeId());
            info.setUnit(device.getUnit());
            info.setStatus(device.getStatus().getValue());
            info.setDeviceState(device.getDeviceState().getValue());
            info.setRemark(device.getRemark());
            info.setCreateTime(device.getCreateTime());
            return info;
        }).toList();

        SensorDeviceResp.QueryList resp = new SensorDeviceResp.QueryList();
        resp.setTotal(deviceInfos.size());
        resp.setDevices(deviceInfos);
        return resp;
    }
}
