package org.jevonD.wastewaterMS.modules.data.service;

import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantDailyDataQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDailyDataQueryResp;

public interface PlantDailySummaryService {

    /**
     * 分页查询水厂关键运行数据
     * @param req 查询条件
     * @return 分页结果
     */
    PlantDailyDataQueryResp queryDailyData(PlantDailyDataQueryReq req);
}