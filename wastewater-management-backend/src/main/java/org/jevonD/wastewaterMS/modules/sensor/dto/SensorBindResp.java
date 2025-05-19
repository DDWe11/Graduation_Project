package org.jevonD.wastewaterMS.modules.sensor.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class SensorBindResp {

    /**
     * 单条绑定记录信息
     */
    @Data
    public static class BindInfo {
        private Long id;                 // 绑定ID
        private Long processUnitId;      // 工艺流程单元ID
        private String processUnitName;  // 工艺单元名称
        private Long sensorId;           // 传感器ID
        private String sensorCode;       // 传感器编码
        private String sensorName;       // 传感器名称
        private LocalDateTime bindTime;  // 绑定时间
    }

    /**
     * 多条绑定记录响应（用于列表）
     */
    @Data
    public static class BindList {
        private Integer total;
        private List<BindInfo> records;
    }

    /**
     * 绑定/解绑操作的简要响应
     */
    @Data
    public static class OperationResp {
        private Long processUnitId;
        private Long sensorId;
        private String message;
    }
}