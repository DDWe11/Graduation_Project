package org.jevonD.wastewaterMS.modules.sensor.service;

import org.jevonD.wastewaterMS.modules.sensor.dto.SensorDeviceReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorDeviceResp;

public interface SensorDeviceService {

    /**
     * 创建传感器设备
     */
    SensorDeviceResp.DeviceInfo createDevice(SensorDeviceReq.CreateSensor req);

    /**
     * 更新传感器设备信息
     */
    SensorDeviceResp.DeviceInfo updateDevice(SensorDeviceReq.UpdateSensorInfo req);

    /**
     * 更新设备在线状态
     */
    SensorDeviceResp.DeviceInfo changeDeviceState(SensorDeviceReq.ChangeDeviceState req);

    /**
     * 删除传感器设备（含返回删除信息）
     */
    SensorDeviceResp.DeleteInfo deleteDevice(SensorDeviceReq.DeleteSensor req);

    /**
     * 修改传感器启用状态（启用 / 禁用）
     */
    SensorDeviceResp.DeviceInfo changeStatus(SensorDeviceReq.ChangeSensorStatus req);

    /**
     * 查询传感器设备列表
     */
    SensorDeviceResp.QueryList listDevices(SensorDeviceReq.QuerySensorDevices req);


}
