package org.jevonD.wastewaterMS.modules.data.dto.websocket;

import lombok.Data;
import java.util.List;

/**
 * 前端发来的WebSocket自动采集参数
 * - sensorIds：采集的传感器ID列表
 * - freq：采集频率（秒），为null或非法时由后端按类型设定默认值
 */
@Data
public class CollectParamsReq {
    private List<Long> sensorIds; // 要采集的传感器id（必填）
    private Integer freq;         // 采集频率，单位：秒（可选/前端建议传，后端兜底）
    private String action;          //开或者关
}
