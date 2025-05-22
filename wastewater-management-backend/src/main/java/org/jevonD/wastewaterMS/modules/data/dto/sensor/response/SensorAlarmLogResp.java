package org.jevonD.wastewaterMS.modules.data.dto.sensor.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SensorAlarmLogResp {
    private Long total;
    private List<AlarmLogItem> data;

    @Data
    public static class AlarmLogItem {
        private Long id;
        private Long sensorId;
        private String sensorName;
        private Double value;
        private String alarmType;     // HIGH/LOW
        private LocalDateTime timestamp;
        private Integer resolved;
        private String resolveRemark;
        private LocalDateTime resolveTime;
    }
}