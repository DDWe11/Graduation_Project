package org.jevonD.wastewaterMS.modules.data.dto.sensor.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SensorDataCollectResp {
    private Long sensorId;          // 传感器ID
    private Long typeId;            // 类型ID
    private String name;            // 传感器名称
    private Double value;           // 采集值
    private String unit;            // 单位
    private LocalDateTime timestamp;// 采集时间
}