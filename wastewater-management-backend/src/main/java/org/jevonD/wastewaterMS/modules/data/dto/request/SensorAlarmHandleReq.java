package org.jevonD.wastewaterMS.modules.data.dto.request;

import lombok.Data;

@Data
public class SensorAlarmHandleReq {
    private Long id;                  // 报警记录ID
    private String resolveRemark;     // 处理备注
}