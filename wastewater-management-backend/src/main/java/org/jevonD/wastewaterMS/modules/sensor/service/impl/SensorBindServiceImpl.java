package org.jevonD.wastewaterMS.modules.sensor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorBindReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorBindResp;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorProcessBind;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorDevice;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorProcessBindRepository;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorDeviceRepository;
import org.jevonD.wastewaterMS.modules.process.entity.ProcessUnit;
import org.jevonD.wastewaterMS.modules.process.repository.ProcessUnitRepository;
import org.jevonD.wastewaterMS.modules.sensor.service.SensorBindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorBindServiceImpl implements SensorBindService {

    private final SensorProcessBindRepository bindRepository;
    private final SensorDeviceRepository sensorDeviceRepository;
    private final ProcessUnitRepository processUnitRepository;

    /**
     * 绑定传感器到指定工艺流程单元
     */
    @Override
    @Transactional
    public SensorBindResp.OperationResp bindSensorToProcess(SensorBindReq.Bind req) {
        // 校验工艺流程单元存在
        ProcessUnit processUnit = processUnitRepository.selectById(req.getProcessUnitId());
        if (processUnit == null) {
            throw new IllegalArgumentException("工艺流程单元不存在");
        }
        // 校验传感器存在
        SensorDevice sensor = sensorDeviceRepository.selectById(req.getSensorId());
        if (sensor == null) {
            throw new IllegalArgumentException("传感器不存在");
        }
        // 校验该传感器是否已被绑定
        boolean exists = bindRepository.existsBindWithSensorId(req.getSensorId());
        if (exists) {
            throw new IllegalStateException("该传感器已绑定其他工艺流程单元，请先解绑");
        }
        // 插入绑定记录
        SensorProcessBind bind = new SensorProcessBind();
        bind.setProcessUnitId(req.getProcessUnitId());
        bind.setSensorId(req.getSensorId());
        bind.setBindTime(LocalDateTime.now());
        bindRepository.insert(bind);

        SensorBindResp.OperationResp resp = new SensorBindResp.OperationResp();
        resp.setProcessUnitId(req.getProcessUnitId());
        resp.setSensorId(req.getSensorId());
        resp.setMessage("绑定成功");
        return resp;
    }

    /**
     * 解绑传感器与工艺流程单元
     */
    @Override
    @Transactional
    public SensorBindResp.OperationResp unbindSensorFromProcess(SensorBindReq.Unbind req) {
        // 查询绑定记录
        SensorProcessBind bind = bindRepository.findByProcessUnitIdAndSensorId(req.getProcessUnitId(), req.getSensorId());
        if (bind == null) {
            throw new IllegalArgumentException("未找到该绑定记录");
        }
        // 删除
        bindRepository.deleteById(bind.getId());

        SensorBindResp.OperationResp resp = new SensorBindResp.OperationResp();
        resp.setProcessUnitId(req.getProcessUnitId());
        resp.setSensorId(req.getSensorId());
        resp.setMessage("解绑成功");
        return resp;
    }

    /**
     * 查询某个流程单元下的所有绑定传感器
     */
    @Override
    public SensorBindResp.BindList getSensorsByProcess(SensorBindReq.QueryByProcess req) {
        List<SensorBindResp.BindInfo> records = bindRepository.findBindInfosByProcessUnitId(req.getProcessUnitId());
        SensorBindResp.BindList resp = new SensorBindResp.BindList();
        resp.setTotal(records.size());
        resp.setRecords(records);
        return resp;
    }

    /**
     * 查询某个传感器当前绑定在哪个流程（如果有）
     */
    @Override
    public SensorBindResp.BindList getProcessesBySensor(SensorBindReq.QueryBySensor req) {
        List<SensorBindResp.BindInfo> records = bindRepository.findBindInfosBySensorId(req.getSensorId());
        SensorBindResp.BindList resp = new SensorBindResp.BindList();
        resp.setTotal(records.size());
        resp.setRecords(records);
        return resp;
    }
}