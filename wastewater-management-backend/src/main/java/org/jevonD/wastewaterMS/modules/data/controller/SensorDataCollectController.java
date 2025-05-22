package org.jevonD.wastewaterMS.modules.data.controller;

import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.request.SensorDataCollectReq;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.response.SensorDataCollectResp;
import org.jevonD.wastewaterMS.modules.data.service.SensorDataCollectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class SensorDataCollectController {

    @Autowired
    private SensorDataCollectService sensorDataCollectService;

    /**
     * 手动采集传感器数据（支持单个、批量、全部采集）
     */
    @PostMapping("/manual-collect")
    public ResponseEntity<ResponseWrapper<List<SensorDataCollectResp>>> manualCollectSensorData(
            @RequestBody SensorDataCollectReq req) {
        try {
            List<SensorDataCollectResp> respList = sensorDataCollectService.collectAndStoreSensorData(req);
            return ResponseEntity.ok(ResponseWrapper.success(respList));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseWrapper.error(500, "服务器内部错误：" + e.getMessage()));
        }
    }
}