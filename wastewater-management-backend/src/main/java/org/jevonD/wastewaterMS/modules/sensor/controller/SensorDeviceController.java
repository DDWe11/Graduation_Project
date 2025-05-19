package org.jevonD.wastewaterMS.modules.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorDeviceReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorDeviceResp;
import org.jevonD.wastewaterMS.modules.sensor.service.SensorDeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor/devices")
@RequiredArgsConstructor
public class SensorDeviceController {

    private final SensorDeviceService sensorDeviceService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<SensorDeviceResp.DeviceInfo>> createDevice(
            @RequestBody @Valid SensorDeviceReq.CreateSensor req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorDeviceService.createDevice(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    @PatchMapping
    public ResponseEntity<ResponseWrapper<SensorDeviceResp.DeviceInfo>> updateDevice(
            @RequestBody @Valid SensorDeviceReq.UpdateSensorInfo req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorDeviceService.updateDevice(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper<SensorDeviceResp.DeleteInfo>> deleteDevice(
            @RequestBody @Valid SensorDeviceReq.DeleteSensor req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorDeviceService.deleteDevice(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    @PutMapping("/status")
    public ResponseEntity<ResponseWrapper<SensorDeviceResp.DeviceInfo>> changeStatus(
            @RequestBody @Valid SensorDeviceReq.ChangeSensorStatus req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorDeviceService.changeStatus(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }
    @PutMapping("/state")
    public ResponseEntity<ResponseWrapper<SensorDeviceResp.DeviceInfo>> changeDeviceState(
            @RequestBody @Valid SensorDeviceReq.ChangeDeviceState req) {
        try {
            SensorDeviceResp.DeviceInfo resp = sensorDeviceService.changeDeviceState(req);
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<SensorDeviceResp.QueryList>> listDevices(
            @Valid SensorDeviceReq.QuerySensorDevices req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorDeviceService.listDevices(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }
}