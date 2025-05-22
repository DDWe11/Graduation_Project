package org.jevonD.wastewaterMS.modules.data.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SensorThresholdResp {
    private Long id;
    private Long sensorId;
    private String sensorName;
    private Double highLimit;
    private Double lowLimit;
    private Integer enabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}