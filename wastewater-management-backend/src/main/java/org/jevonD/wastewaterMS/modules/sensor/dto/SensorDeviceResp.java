package org.jevonD.wastewaterMS.modules.sensor.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class SensorDeviceResp {

    /**
     * 设备详细信息，用于创建、更新、详情返回
     */
    @Data
    public static class DeviceInfo {
        private Long id;              // 主键ID
        private String code;          // 编码，如 COD-001
        private String name;          // 名称，如 COD传感器
        private Long typeId;          // 类型ID
        private String unit;          // 单位
        private Integer status;       // 启用状态（0禁用，1启用）
        private Integer deviceState; // 设备实时状态
        private String remark;        // 备注信息
        private LocalDateTime createTime;  // 创建时间
    }

    /**
     * 查询分页响应
     */
    @Data
    public static class QueryList {
        private Integer total;                   // 总条数
        private List<DeviceInfo> devices;        // 设备列表
    }

    @Data
    public static class DeleteInfo {
        private Long id;                 // 设备ID
        private String code;             // 设备编码
        private String name;             // 设备名称
        private LocalDateTime deletedTime; // 删除时间
    }
}