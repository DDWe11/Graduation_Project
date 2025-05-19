package org.jevonD.wastewaterMS.modules.sensor.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorDeviceReq;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorDevice;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mapper
public interface SensorDeviceRepository extends BaseMapper<SensorDevice> {
    /**
     * 是否已存在指定编码的设备
     */
    @Select("SELECT COUNT(*) FROM sensor_device WHERE code = #{code} LIMIT 1")
    boolean existsByCode(@Param("code") String code);

    /**
     * 获取当前最大 ID
     */
    @Select("SELECT COALESCE(MAX(id), 0) FROM sensor_device FOR UPDATE")
    Long selectMaxId();

    /**
     * 返回 +1 后的新 ID（服务层调用）
     */
    default Long generateNextId() {
        Long maxId = selectMaxId();
        return (maxId == null) ? 1L : maxId + 1;
    }
    @Select("""
    SELECT COUNT(*) FROM sensor_device 
    WHERE type_id = #{typeId}
        AND DATE_FORMAT(create_time, '%y%m%d') = #{datePrefix}
    """)
    int countByTypeToday(@Param("typeId") Long typeId, @Param("datePrefix") String datePrefix);

    /**
     * 条件查询传感器设备列表（支持多字段组合）
     */
    default List<SensorDevice> findByConditions(SensorDeviceReq.QuerySensorDevices req) {
        QueryWrapper<SensorDevice> wrapper = new QueryWrapper<>();

        if (req.getId() != null) {
            wrapper.eq("id", req.getId());
        }

        if (req.getCode() != null && !req.getCode().isEmpty()) {
            wrapper.like("code", req.getCode());
        }

        if (req.getName() != null && !req.getName().isEmpty()) {
            wrapper.like("name", req.getName());
        }

        if (req.getStatus() != null) {
            wrapper.eq("status", req.getStatus());
        }

        if (req.getDeviceState() != null) {
            wrapper.eq("device_state", req.getDeviceState());
        }

        if (req.getStartTime() != null) {
            wrapper.ge("create_time", req.getStartTime());
        }

        if (req.getEndTime() != null) {
            wrapper.le("create_time", req.getEndTime());
        }

        return selectList(wrapper);
    }
    /**
     * 根据设备ID列表、设备编号、状态、设备运行状态动态查询传感器设备
     *
     * @param sensorIds   可选，传感器ID列表
     * @param codes       可选，传感器编号列表
     * @param status      状态，1-启用
     * @param deviceStates 在线/异常等状态
     * @return 符合条件的设备列表
     */
    default List<SensorDevice> findByIdsAndCodes(
            List<Long> sensorIds,
            List<String> codes,
            Integer status,
            List<Integer> deviceStates) {
        QueryWrapper<SensorDevice> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (deviceStates != null && !deviceStates.isEmpty()) {
            wrapper.in("device_state", deviceStates);
        }
        if (sensorIds != null && !sensorIds.isEmpty()) {
            wrapper.in("id", sensorIds);
        }
        if (codes != null && !codes.isEmpty()) {
            wrapper.in("code", codes);
        }
        return selectList(wrapper);
    }
    // 如需保留原无参方法，可加一行调用
    default List<SensorDevice> findEnabledAndActiveDevices() {
        return findByIdsAndCodes(null, null, 1, Arrays.asList(1, 2));
    }

    List<Long> findUnboundSensorIds();

    List<SensorDevice> findByIdsAndTypeAndState(
            @Param("sensorIds") List<Long> sensorIds,
            @Param("sensorTypeCode") String sensorTypeCode,
            @Param("deviceState") Integer deviceState
    );

    List<SensorDevice> findDevicesByIds(@Param("ids") Collection<Long> ids);
}
