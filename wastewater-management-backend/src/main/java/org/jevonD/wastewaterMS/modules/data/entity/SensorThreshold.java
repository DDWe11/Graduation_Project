package org.jevonD.wastewaterMS.modules.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sensor_threshold")
public class SensorThreshold {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sensorId;
    private Double highLimit;
    private Double lowLimit;
    private Integer enabled; // 1启用0禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
