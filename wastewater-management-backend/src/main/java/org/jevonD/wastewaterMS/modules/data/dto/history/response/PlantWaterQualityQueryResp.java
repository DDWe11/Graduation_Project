package org.jevonD.wastewaterMS.modules.data.dto.history.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PlantWaterQualityQueryResp {
    private Long total;
    private List<PlantWaterQualityItem> records;

    @Data
    public static class PlantWaterQualityItem {
        private Long id; // 分页序号（service中赋值）
        private LocalDate recordDate;
        private Double inWaterVolume;
        private Double outWaterVolume;
        private Double inPh;
        private Double inCod;
        private Double inSs;
        private Double inTp;
        private Double inTn;
        private Double inNh3n;
        private Double outPh;
        private Double outCod;
        private Double outSs;
        private Double outTp;
        private Double outTn;
        private Double outNh3n;
    }
}