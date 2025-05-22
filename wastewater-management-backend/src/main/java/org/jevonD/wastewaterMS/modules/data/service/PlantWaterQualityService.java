package org.jevonD.wastewaterMS.modules.data.service;

import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantWaterQualityQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantWaterQualityQueryResp;

public interface PlantWaterQualityService {
    PlantWaterQualityQueryResp queryWaterQuality(PlantWaterQualityQueryReq req);
}