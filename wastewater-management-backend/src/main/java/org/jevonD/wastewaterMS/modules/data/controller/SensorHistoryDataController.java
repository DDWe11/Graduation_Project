package org.jevonD.wastewaterMS.modules.data.controller;

import jakarta.validation.Valid;
import org.jevonD.wastewaterMS.modules.data.dto.request.SensorHistoryDataQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.response.SensorHistoryDataQueryResp;
import org.jevonD.wastewaterMS.modules.data.service.SensorHistoryDataQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.jevonD.wastewaterMS.common.response.ResponseWrapper; // 按你的实际路径
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data/sensorhistory")
public class SensorHistoryDataController {

    @Autowired
    private SensorHistoryDataQueryService sensorHistoryDataQueryService;

    /**
     * 分页条件查询传感器历史数据
     */
    @PostMapping("/page")
    public ResponseEntity<ResponseWrapper<SensorHistoryDataQueryResp>> querySensorHistoryData(
            @Valid @RequestBody SensorHistoryDataQueryReq req) {
        try {
            SensorHistoryDataQueryResp resp = sensorHistoryDataQueryService.querySensorHistoryData(req);
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (IllegalArgumentException e) {
            // 业务型异常：如参数不合法等
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            // 其他异常（系统异常）
            return ResponseEntity.status(500).body(ResponseWrapper.error(500, "系统异常: " + e.getMessage()));
        }
    }
}