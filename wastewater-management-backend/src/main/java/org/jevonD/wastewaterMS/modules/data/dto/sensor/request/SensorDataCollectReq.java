// org.jevonD.wastewaterMS.modules.data.dto.request.SensorDataCollectReq.java
package org.jevonD.wastewaterMS.modules.data.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class SensorDataCollectReq {
    // 可扩展指定采集设备列表
    private List<Long> sensorIds;
    private List<String> code;
}