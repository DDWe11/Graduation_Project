package org.jevonD.wastewaterMS.modules.sensor.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jevonD.wastewaterMS.modules.sensor.dto.SensorBindResp;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorProcessBind;

import java.util.List;

@Mapper
public interface SensorProcessBindRepository extends BaseMapper<SensorProcessBind> {
    @Select("SELECT COUNT(*) FROM process_sensor_bind WHERE sensor_id = #{sensorId} LIMIT 1")
    boolean existsBindWithSensorId(@Param("sensorId") Long sensorId);

    @Select("SELECT * FROM process_sensor_bind WHERE process_unit_id = #{processUnitId} AND sensor_id = #{sensorId} LIMIT 1")
    SensorProcessBind findByProcessUnitIdAndSensorId(@Param("processUnitId") Long processUnitId, @Param("sensorId") Long sensorId);

    // 多表联查：查工艺单元下的所有传感器，返回DTO
    @Select("""
        SELECT 
            b.id,
            b.process_unit_id AS processUnitId,
            pu.name AS processUnitName,
            b.sensor_id AS sensorId,
            sd.code AS sensorCode,
            sd.name AS sensorName,
            b.bind_time AS bindTime
        FROM process_sensor_bind b
        LEFT JOIN process_unit pu ON b.process_unit_id = pu.id
        LEFT JOIN sensor_device sd ON b.sensor_id = sd.id
        WHERE b.process_unit_id = #{processUnitId}
    """)
    List<SensorBindResp.BindInfo> findBindInfosByProcessUnitId(@Param("processUnitId") Long processUnitId);

    @Select("""
    SELECT 
        b.id,
        b.process_unit_id AS processUnitId,
        pu.name AS processUnitName,
        b.sensor_id AS sensorId,
        sd.code AS sensorCode,
        sd.name AS sensorName,
        b.bind_time AS bindTime
    FROM process_sensor_bind b
    LEFT JOIN process_unit pu ON b.process_unit_id = pu.id
    LEFT JOIN sensor_device sd ON b.sensor_id = sd.id
    WHERE b.sensor_id = #{sensorId}
    """)
    List<SensorBindResp.BindInfo> findBindInfosBySensorId(@Param("sensorId") Long sensorId);

    /**
     * 查找绑定在出水口（process_unit_id=8）的传感器ID
     */
    @Select("SELECT sensor_id FROM process_sensor_bind WHERE process_unit_id = 8")
    List<Long> findOutletSensorIds();

    /**
     * 查找绑定在进水口（process_unit_id=1）的传感器ID
     */
    @Select("SELECT sensor_id FROM process_sensor_bind WHERE process_unit_id = 1")
    List<Long> findInletSensorIds();


    @Select("""
        SELECT sensor_id 
        FROM process_sensor_bind 
        WHERE process_unit_id = #{processUnitId}
    """)
    List<Long> findSensorIdsByProcessUnitId(@Param("processUnitId") Long processUnitId);
}
