package org.jevonD.wastewaterMS.modules.data.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 传感器历史数据分页查询响应DTO
 */
@Data
public class SensorHistoryDataQueryResp {
    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页数据
     */
    private List<SensorHistoryDataItem> records;

    @Data
    public static class SensorHistoryDataItem {
        private Long id;
        private Long sensorId;
        private String sensorTypeCode;
        private String sensorTypeName; // eg. 流量
        private Double value;
        private String unit;
        private LocalDateTime timestamp;
        private Integer deviceState;
        private Integer alarmFlag;
        private String remark;
    }
}