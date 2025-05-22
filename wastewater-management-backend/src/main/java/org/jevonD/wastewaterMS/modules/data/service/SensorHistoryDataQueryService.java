package org.jevonD.wastewaterMS.modules.data.service;

import org.jevonD.wastewaterMS.modules.data.dto.sensor.request.SensorHistoryDataQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.response.SensorHistoryDataQueryResp;

/**
 * 传感器历史数据查询服务
 */
public interface SensorHistoryDataQueryService {
    /**
     * 分页多条件查询传感器历史数据
     *
     * @param req 查询请求（包含工艺、传感器、类型、报警、时间区间、分页等）
     * @return 分页结果
     */
    SensorHistoryDataQueryResp querySensorHistoryData(SensorHistoryDataQueryReq req);
}