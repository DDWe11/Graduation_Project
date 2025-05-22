package org.jevonD.wastewaterMS.modules.data.service;

import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantDrugUsageQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDrugUsageQueryResp;

public interface PlantDrugUsageService {
    /**
     * 分页查询用药量明细
     */
    PlantDrugUsageQueryResp queryDrugUsage(PlantDrugUsageQueryReq req);
}