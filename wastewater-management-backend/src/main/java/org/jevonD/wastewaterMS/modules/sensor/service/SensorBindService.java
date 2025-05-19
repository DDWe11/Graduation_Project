package org.jevonD.wastewaterMS.modules.sensor.service;

import org.jevonD.wastewaterMS.modules.sensor.dto.SensorBindReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorBindResp;

public interface SensorBindService {

    /**
     * 绑定传感器到指定工艺流程单元
     */
    SensorBindResp.OperationResp bindSensorToProcess(SensorBindReq.Bind req);

    /**
     * 解绑传感器与工艺流程单元
     */
    SensorBindResp.OperationResp unbindSensorFromProcess(SensorBindReq.Unbind req);

    /**
     * 查询某个流程单元下的所有绑定传感器
     */
    SensorBindResp.BindList getSensorsByProcess(SensorBindReq.QueryByProcess req);

    /**
     * 查询某个传感器当前绑定在哪个流程（如果有）
     */
    SensorBindResp.BindList getProcessesBySensor(SensorBindReq.QueryBySensor req);
}