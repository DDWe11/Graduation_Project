package org.jevonD.wastewaterMS.modules.data.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jevonD.wastewaterMS.modules.data.entity.SensorAlarmLog;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SensorAlarmLogRepository extends BaseMapper<SensorAlarmLog> {
    // 查询报警日志（按传感器、时间、类型、处理状态等过滤）
    List<SensorAlarmLog> selectAlarmLogs(
            @Param("sensorId") Long sensorId,
            @Param("alarmType") String alarmType,
            @Param("resolved") Integer resolved,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
