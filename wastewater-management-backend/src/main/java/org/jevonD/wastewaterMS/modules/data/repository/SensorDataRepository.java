package org.jevonD.wastewaterMS.modules.data.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jevonD.wastewaterMS.modules.data.entity.SensorData;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SensorDataRepository extends BaseMapper<SensorData> {
    // 查询某传感器指定时间段内的历史数据
    List<SensorData> selectHistory(
            @Param("sensorId") Long sensorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("alarmFlag") Integer alarmFlag);

    // 查询所有传感器最新一条数据（大屏展示用）
    List<SensorData> selectLatestForAllSensors();
    // 批量插入方法
    void insertBatch(@Param("list") List<SensorData> dataList);

    List<SensorData> findPageByConditions(
            @Param("page") Page<SensorData> page,
            @Param("sensorIds") List<Long> sensorIds,
            @Param("alarmFlag") Integer alarmFlag,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}