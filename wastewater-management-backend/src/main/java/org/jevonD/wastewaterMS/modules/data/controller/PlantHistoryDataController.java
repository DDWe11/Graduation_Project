package org.jevonD.wastewaterMS.modules.data.controller;

import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantDailyDataQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantDrugUsageQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantWaterQualityQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDailyDataQueryResp;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDrugUsageQueryResp;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantWaterQualityQueryResp;
import org.jevonD.wastewaterMS.modules.data.repository.PlantWaterQualityRepository;
import org.jevonD.wastewaterMS.modules.data.service.PlantDailySummaryService;
import org.jevonD.wastewaterMS.modules.data.service.PlantDrugUsageService;
import org.jevonD.wastewaterMS.modules.data.service.PlantWaterQualityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/data/history")
@RequiredArgsConstructor
public class PlantHistoryDataController {

    private final PlantDailySummaryService plantDailySummaryService;
    private final PlantWaterQualityService plantWaterQualityService;
    private final PlantDrugUsageService plantDrugUsageService;

    @PostMapping("/overlook")
    public ResponseEntity<ResponseWrapper<PlantDailyDataQueryResp>> queryDailyOverview(
            @RequestBody @Valid PlantDailyDataQueryReq req) {
        try {
            PlantDailyDataQueryResp resp = plantDailySummaryService.queryDailyData(req);
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "服务器内部异常"));
        }
    }

    @PostMapping("/water-quality")
    public ResponseEntity<ResponseWrapper<PlantWaterQualityQueryResp>> queryWaterQuality(
            @RequestBody @Valid PlantWaterQualityQueryReq req) {
        try {
            PlantWaterQualityQueryResp resp = plantWaterQualityService.queryWaterQuality(req);
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "服务器内部异常"));
        }
    }

    @PostMapping("/drug-usage")
    public ResponseEntity<ResponseWrapper<PlantDrugUsageQueryResp>> queryDrugUsage(
            @RequestBody @Valid PlantDrugUsageQueryReq req) {
        try {
            PlantDrugUsageQueryResp resp = plantDrugUsageService.queryDrugUsage(req);
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "服务器内部异常"));
        }
    }

}