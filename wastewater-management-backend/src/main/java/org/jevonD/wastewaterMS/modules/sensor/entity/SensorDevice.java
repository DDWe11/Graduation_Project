package org.jevonD.wastewaterMS.modules.sensor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.jevonD.wastewaterMS.modules.sensor.entity.enums.SensorStatus;
import org.jevonD.wastewaterMS.modules.sensor.entity.enums.DeviceState;

import java.time.LocalDateTime;

/**
 * 传感器设备实体
 */
@Data
@TableName("sensor_device")
public class SensorDevice {

    @TableId(type = IdType.ASSIGN_ID) // 使用雪花算法或手动赋值
    private Long id;

    private String code; // 传感器编码，如 COD-001

    private String name; // 传感器名称

    private Long typeId; // 对应 sensor_type 表的主键

    private String unit; // 测量单位

    @EnumValue
    private SensorStatus status; // 启用状态（0=禁用，1=启用）

    @EnumValue
    private DeviceState deviceState; // 设备状态（0=离线，1=在线，2=异常）

    private String remark; // 备注

    private LocalDateTime createTime; // 创建时间
}