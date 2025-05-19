package org.jevonD.wastewaterMS.modules.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorTypeReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorTypeResp;
import org.jevonD.wastewaterMS.modules.sensor.service.SensorTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor/types")
@RequiredArgsConstructor
public class SensorTypeController {

    private final SensorTypeService sensorTypeService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<SensorTypeResp.addResp>> addSensorType(@Valid @RequestBody SensorTypeReq.addReq req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorTypeService.addSensorType(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper<SensorTypeResp.updateResp>> updateSensorType(@Valid @RequestBody SensorTypeReq.updateReq req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorTypeService.updateSensorType(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper<SensorTypeResp.deleteResp>> deleteSensorType(@Valid @RequestBody SensorTypeReq.deleteReq req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorTypeService.deleteSensorType(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<SensorTypeResp.queryResp>> querySensorTypes(@Valid SensorTypeReq.queryReq req) {
        return ResponseEntity.ok(ResponseWrapper.success(sensorTypeService.querySensorType(req)));
    }
}