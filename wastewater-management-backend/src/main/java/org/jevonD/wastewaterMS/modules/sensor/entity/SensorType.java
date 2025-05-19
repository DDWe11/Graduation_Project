package org.jevonD.wastewaterMS.modules.sensor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sensor_type")
public class SensorType {

    @TableId(type = IdType.INPUT)
    private Long id; // 传感器类型ID（自增主键）

    private String code; // 类型编码，如 COD、PH、FLOW

    private String name; // 类型名称（中文）

    private String defaultUnit; // 默认单位

    private String description; // 类型说明

    private String model; // 传感器型号
}
