package org.jevonD.wastewaterMS.modules.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sensor_alarm_log")
public class SensorAlarmLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sensorId;
    private Double value;
    private String alarmType; // "HIGH" or "LOW"
    private LocalDateTime timestamp;
    private Integer resolved; // 0未处理 1已处理
    private String resolveRemark;
    private LocalDateTime resolveTime;
}
