package org.jevonD.wastewaterMS.modules.data.dto.sensor.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SensorAlarmLogReq {
    private Long sensorId;              // 可选
    private String alarmType;           // HIGH/LOW/ALL
    private Integer resolved;           // 0未处理/1已处理/ALL
    private LocalDateTime startTime;    // 查询起始
    private LocalDateTime endTime;      // 查询结束
    private Integer page = 1;
    private Integer size = 20;
}