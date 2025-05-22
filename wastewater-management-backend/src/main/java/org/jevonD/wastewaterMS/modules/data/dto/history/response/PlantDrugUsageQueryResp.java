package org.jevonD.wastewaterMS.modules.data.dto.history.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PlantDrugUsageQueryResp {
    private Long total;
    private List<PlantDrugUsageItem> records;

    @Data
    public static class PlantDrugUsageItem {
        private Long id; // 序号（service层赋值）
        private LocalDate recordDate;
        private Double pamCationicDaily;
        private Double sodiumAcetate30Daily;
        private Double pac10Daily;
        private Double sodiumHypochlorite10Daily;
        private Double pamAnionic5Daily;
        private Double pamCationicDrugPerTonSludge;
        private Double sodiumAcetate30PerM3;
        private Double pac10PerM3;
        private Double sodiumHypochlorite10PerM3;
        private Double pamAnionicPerM3;
    }
}
