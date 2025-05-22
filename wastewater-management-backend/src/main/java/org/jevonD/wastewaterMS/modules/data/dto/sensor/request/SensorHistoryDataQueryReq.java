package org.jevonD.wastewaterMS.modules.data.dto.sensor.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 传感器历史数据分页查询请求DTO
 */
@Data
public class SensorHistoryDataQueryReq {
    private Long processUnitId;          // 工艺流程ID（可选）
    private List<Long> sensorIds;        // 传感器ID列表（可选）
    private String sensorTypeCode;       // 类型编码，如FLOW
    private Integer deviceState;         // 传感器状态（0离线、1在线、2异常）
    private Integer alarmFlag;           // 是否报警（0/1）
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer page;                // 当前页码（前端必传）

    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页最小为1")
    @Max(value = 50, message = "每页最大50条")
    private Integer size;                // 每页条数（前端必传）
    private Boolean globalOnly;          // 全局（未绑定）查询标志
}
