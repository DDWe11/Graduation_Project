package org.jevonD.wastewaterMS.modules.data.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SensorLatestDataResp {
    private List<SensorLatestItem> sensors;

    @Data
    public static class SensorLatestItem {
        private Long sensorId;
        private String sensorName;
        private String type;            // 类型，如“流量”、“pH”
        private Double value;
        private String unit;
        private LocalDateTime timestamp;
        private Integer alarmFlag;
    }
}
