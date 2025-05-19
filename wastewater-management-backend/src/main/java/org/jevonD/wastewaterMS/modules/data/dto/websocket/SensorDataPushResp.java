package org.jevonD.wastewaterMS.modules.data.dto.websocket;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SensorDataPushResp {
    private Long sensorId;
    private Long typeId;
    private String name;
    private Double value;
    private String unit;
    private LocalDateTime timestamp;
}