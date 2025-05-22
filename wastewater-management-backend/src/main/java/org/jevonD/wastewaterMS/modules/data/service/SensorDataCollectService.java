package org.jevonD.wastewaterMS.modules.data.service;

import org.jevonD.wastewaterMS.modules.data.dto.sensor.request.SensorDataCollectReq;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.response.SensorDataCollectResp;
import java.util.List;

public interface SensorDataCollectService {
    /**
     * 采集指定传感器或全部传感器的实时数据，数据模拟生成并存入历史表，同时返回采集结果。
     *
     * @param req 采集请求
     * @return 实时采集数据列表
     */
    List<SensorDataCollectResp> collectAndStoreSensorData(SensorDataCollectReq req);
}