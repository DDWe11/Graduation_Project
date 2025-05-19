package org.jevonD.wastewaterMS.modules.sensor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class SensorTypeReq {

    @Data
    public static class addReq {
        @NotBlank(message = "传感器类型编码不能为空")
        private String code;

        @NotBlank(message = "传感器类型名称不能为空")
        private String name;

        @NotBlank(message = "传感器单位不能为空")
        private String defaultUnit;

        private String description;

        private String model;
    }

    @Data
    public static class updateReq {
        @NotNull(message = "传感器ID不能为空")
        private Long id;

        private String code;

        private String name;

        private String defaultUnit;

        private String description;

        private String model;
    }

    @Data
    public static class deleteReq {
        @NotNull(message = "传感器ID不能为空")
        private Long id;
    }


    @Data
    public static class queryReq {
        private String code;
        private String name;
    }
}
