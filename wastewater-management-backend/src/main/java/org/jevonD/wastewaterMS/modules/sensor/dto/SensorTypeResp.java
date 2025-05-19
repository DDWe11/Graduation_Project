package org.jevonD.wastewaterMS.modules.sensor.dto;

import lombok.Data;
import java.util.List;

public class SensorTypeResp {
    @Data
    public static class addResp{
        private Long id; // 传感器类型ID（自增主键）

        private String code; // 类型编码，如 COD、PH、FLOW

        private String name; // 类型名称（中文）

        private String defaultUnit; // 默认单位

        private String description; // 类型说明

        private String model; // 传感器型号
    }

    @Data
    public static class updateResp{
        private Long id; // 传感器类型ID（自增主键）

        private String code; // 类型编码，如 COD、PH、FLOW

        private String name; // 类型名称（中文）

        private String defaultUnit; // 默认单位

        private String description; // 类型说明

        private String model; // 传感器型号
    }

    @Data
    public static class deleteResp{
        private Long id; // 传感器类型ID（自增主键）
        private String code; // 类型编码，如 COD、PH、FLOW
        private String name; // 类型名称（中文）
    }

    @Data
    public static class queryResp{
        private Integer total;
        private List<SensorTypeInfo> sensorTypes;

        @Data
        public static class SensorTypeInfo{
            private Long id;            // 类型ID
            private String code;        // 类型编码，如 COD、PH、FLOW
            private String name;        // 类型名称（中文）
            private String defaultUnit; // 默认单位
            private String description; // 类型说明
            private String model;       // 型号
        }
    }
}
