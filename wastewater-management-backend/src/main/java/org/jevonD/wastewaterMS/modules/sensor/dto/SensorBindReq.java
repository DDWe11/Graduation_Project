package org.jevonD.wastewaterMS.modules.sensor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class SensorBindReq {

    /**
     * 绑定请求
     */
    @Data
    public static class Bind {
        @NotNull(message = "工艺流程单元ID不能为空")
        private Long processUnitId;

        @NotNull(message = "传感器ID不能为空")
        private Long sensorId;
    }

    /**
     * 解绑请求
     */
    @Data
    public static class Unbind {
        @NotNull(message = "工艺流程单元ID不能为空")
        private Long processUnitId;

        @NotNull(message = "传感器ID不能为空")
        private Long sensorId;
    }

    /**
     * 查询绑定记录（按工艺单元ID）
     */
    @Data
    public static class QueryByProcess {
        @NotNull(message = "工艺流程单元ID不能为空")
        private Long processUnitId;
    }

    /**
     * 查询绑定记录（按传感器ID）
     */
    @Data
    public static class QueryBySensor {
        @NotNull(message = "传感器ID不能为空")
        private Long sensorId;
    }
}
