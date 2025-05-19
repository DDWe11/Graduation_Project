package org.jevonD.wastewaterMS.modules.sensor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

public class SensorDeviceReq {

    @Data
    public static class CreateSensor {

        @NotNull(message = "传感器类型ID不能为空")
        private Long typeId;

        private String remark;

        private Integer status; // 默认为启用
    }

    @Data
    public static class UpdateSensorInfo {
        @NotNull(message = "传感器ID不能为空")
        private Long id;

        private String remark;
    }

    @Data
    public static class DeleteSensor {
        @NotNull(message = "传感器ID不能为空")
        private Long id;
    }

    @Data
    public static class ChangeSensorStatus {
        @NotNull(message = "传感器ID不能为空")
        private Long id;

        @NotNull(message = "状态不能为空")
        private Integer status; // 0=禁用, 1=启用
    }

    @Data
    public static class ChangeDeviceState {
        @NotNull(message = "传感器ID不能为空")
        private Long id;

        @NotNull(message = "设备状态不能为空")
        private Integer deviceState; // 0=离线, 1=在线, 2=异常
    }

    @Data
    public static class QuerySensorDevices {

        private Long id;                  // 精确匹配设备 ID

        private String code;              // 模糊或精确匹配设备编码

        private String name;              // 模糊匹配名称

        private Integer status;           // 启用状态（0=禁用，1=启用）

        private Integer deviceState; // 设备实时状态

        private LocalDateTime startTime;  // 创建时间范围-起始

        private LocalDateTime endTime;    // 创建时间范围-结束
    }
}
