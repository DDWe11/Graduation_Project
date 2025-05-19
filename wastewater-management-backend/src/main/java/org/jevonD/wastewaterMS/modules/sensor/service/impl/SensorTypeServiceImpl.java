// SensorTypeServiceImpl.java（实现类）
package org.jevonD.wastewaterMS.modules.sensor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorTypeReq;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorTypeResp;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorType;
import org.jevonD.wastewaterMS.modules.sensor.repository.SensorTypeRepository;
import org.jevonD.wastewaterMS.modules.sensor.service.SensorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorTypeServiceImpl extends ServiceImpl<SensorTypeRepository, SensorType> implements SensorTypeService {

    @Autowired
    private final SensorTypeRepository sensorTypeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorTypeResp.addResp addSensorType(SensorTypeReq.addReq addReq) {
        SensorType existByCode = sensorTypeRepository.findByCode(addReq.getCode());
        if (existByCode != null) {
            throw new IllegalArgumentException("传感器编码 " + addReq.getCode() + " 已存在");
        }

        SensorType existByName = sensorTypeRepository.findByName(addReq.getName());
        if (existByName != null) {
            throw new IllegalArgumentException("传感器名称 \"" + addReq.getName() + "\" 已存在");
        }

        Long maxId = sensorTypeRepository.selectMaxId(); // 带锁
        Long newSensorTypeId = (maxId == null) ? 1L : maxId + 1;

        SensorType type = new SensorType();
        type.setId(newSensorTypeId);
        type.setCode(addReq.getCode());
        type.setName(addReq.getName());
        type.setDefaultUnit(addReq.getDefaultUnit());
        type.setDescription(addReq.getDescription());
        type.setModel(addReq.getModel());
        sensorTypeRepository.insert(type);

        SensorTypeResp.addResp resp = new SensorTypeResp.addResp();
        resp.setId(type.getId());
        resp.setCode(type.getCode());
        resp.setName(type.getName());
        resp.setDefaultUnit(type.getDefaultUnit());
        resp.setDescription(type.getDescription());
        resp.setModel(type.getModel());
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorTypeResp.deleteResp deleteSensorType(SensorTypeReq.deleteReq deleteReq) {
        SensorType type = sensorTypeRepository.selectById(deleteReq.getId());
        if (type == null) {
            throw new IllegalArgumentException("传感器类型不存在");
        }
        sensorTypeRepository.deleteById(deleteReq.getId());

        SensorTypeResp.deleteResp resp = new SensorTypeResp.deleteResp();
        resp.setId(type.getId());
        resp.setCode(type.getCode());
        resp.setName(type.getName());
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SensorTypeResp.updateResp updateSensorType(SensorTypeReq.updateReq updateReq) {
        SensorType type = sensorTypeRepository.selectById(updateReq.getId());
        if (type == null) {
            throw new IllegalArgumentException("传感器类型不存在");
        }

        // 校验 code 是否重复（排除自身）
        if (updateReq.getCode() != null && !updateReq.getCode().equals(type.getCode())) {
            SensorType existing = sensorTypeRepository.findByCode(updateReq.getCode());
            if (existing != null && !existing.getId().equals(updateReq.getId())) {
                throw new IllegalArgumentException("传感器编码 " + updateReq.getCode() + " 已存在");
            }
            type.setCode(updateReq.getCode());
        }

        // 校验 name 是否重复（排除自身）
        if (updateReq.getName() != null && !updateReq.getName().equals(type.getName())) {
            SensorType existing = sensorTypeRepository.findByName(updateReq.getName());
            if (existing != null && !existing.getId().equals(updateReq.getId())) {
                throw new IllegalArgumentException("传感器名称 \"" + updateReq.getName() + "\" 已存在");
            }
            type.setName(updateReq.getName());
        }

        if (updateReq.getDefaultUnit() != null) {
            type.setDefaultUnit(updateReq.getDefaultUnit());
        }
        if (updateReq.getDescription() != null) {
            type.setDescription(updateReq.getDescription());
        }
        if (updateReq.getModel() != null) {
            type.setModel(updateReq.getModel());
        }

        sensorTypeRepository.updateById(type);

        SensorTypeResp.updateResp resp = new SensorTypeResp.updateResp();
        resp.setId(type.getId());
        resp.setCode(type.getCode());
        resp.setName(type.getName());
        resp.setDefaultUnit(type.getDefaultUnit());
        resp.setDescription(type.getDescription());
        resp.setModel(type.getModel());
        return resp;
    }

    @Override
    public SensorTypeResp.queryResp querySensorType(SensorTypeReq.queryReq queryReq) {
        List<SensorType> list = sensorTypeRepository.findByConditions(queryReq.getCode(), queryReq.getName());

        List<SensorTypeResp.queryResp.SensorTypeInfo> sensorTypes = list.stream().map(type -> {
            SensorTypeResp.queryResp.SensorTypeInfo info = new SensorTypeResp.queryResp.SensorTypeInfo();
            info.setId(type.getId());
            info.setCode(type.getCode());
            info.setName(type.getName());
            info.setDefaultUnit(type.getDefaultUnit());
            info.setDescription(type.getDescription());
            info.setModel(type.getModel());
            return info;
        }).toList();

        SensorTypeResp.queryResp resp = new SensorTypeResp.queryResp();
        resp.setTotal(sensorTypes.size());
        resp.setSensorTypes(sensorTypes);
        return resp;
    }
}
