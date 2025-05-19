package org.jevonD.wastewaterMS.modules.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorBindReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorBindResp;
import org.jevonD.wastewaterMS.modules.sensor.service.SensorBindService;
import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor/bind")
@RequiredArgsConstructor
public class SensorBindController {

    private final SensorBindService sensorBindService;

    /** 1. 绑定传感器到工艺流程单元 */
    @PostMapping
    public ResponseEntity<ResponseWrapper<SensorBindResp.OperationResp>> bindSensor(
            @RequestBody @Valid SensorBindReq.Bind req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorBindService.bindSensorToProcess(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "服务器异常"));
        }
    }

    /** 2. 解绑传感器与工艺流程单元 */
    @PostMapping("/unbind")
    public ResponseEntity<ResponseWrapper<SensorBindResp.OperationResp>> unbindSensor(
            @RequestBody @Valid SensorBindReq.Unbind req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorBindService.unbindSensorFromProcess(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "服务器异常"));
        }
    }

    /** 3. 查询某工艺单元下所有绑定传感器 */
    @GetMapping("/by-process")
    public ResponseEntity<ResponseWrapper<SensorBindResp.BindList>> getSensorsByProcess(
            @Valid SensorBindReq.QueryByProcess req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorBindService.getSensorsByProcess(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "服务器异常"));
        }
    }

    /** 4. 查询某传感器绑定在哪些工艺流程单元 */
    @GetMapping("/by-sensor")
    public ResponseEntity<ResponseWrapper<SensorBindResp.BindList>> getProcessesBySensor(
            @Valid SensorBindReq.QueryBySensor req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(sensorBindService.getProcessesBySensor(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "服务器异常"));
        }
    }
}