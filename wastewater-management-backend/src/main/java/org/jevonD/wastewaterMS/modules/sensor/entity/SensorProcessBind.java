package org.jevonD.wastewaterMS.modules.sensor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("process_sensor_bind")
public class SensorProcessBind {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long processUnitId; // 工艺单元ID

    private Long sensorId; // 传感器ID

    private LocalDateTime bindTime; // 绑定时间

    private String position; // 可选增强字段：如 INLET、OUTLET、INSIDE

    private String remark; // 备注（如测试传感器）
}
