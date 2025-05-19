package org.jevonD.wastewaterMS.modules.sensor.service;

import org.jevonD.wastewaterMS.modules.sensor.dto.SensorTypeReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorTypeResp;

public interface SensorTypeService  {

    SensorTypeResp.addResp addSensorType(SensorTypeReq.addReq addReq);

    SensorTypeResp.deleteResp deleteSensorType(SensorTypeReq.deleteReq deleteReq);

    SensorTypeResp.updateResp updateSensorType(SensorTypeReq.updateReq updateReq);

    SensorTypeResp.queryResp querySensorType(SensorTypeReq.queryReq queryReq);
}
