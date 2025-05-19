package org.jevonD.wastewaterMS.modules.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 传感器采集数据实体
 * 对应表 sensor_data
 */
@Data
@TableName("sensor_data")
public class SensorData {

    /**
     * 数据ID，自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 传感器ID，关联 sensor_device(id)
     */
    @TableField("sensor_id")
    private Long sensorId;

    /**
     * 采集值
     */
    @TableField("value")
    private Double value;

    /**
     * 采集时间
     */
    @TableField("timestamp")
    private LocalDateTime timestamp;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否报警（0否 1是）
     */
    @TableField("alarm_flag")
    private Integer alarmFlag;
}