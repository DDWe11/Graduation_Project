package org.jevonD.wastewaterMS.modules.data.dto.history.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 水厂每日关键数据分页响应DTO
 */
@Data
public class PlantDailyDataQueryResp {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页数据
     */
    private List<PlantDailyDataItem> records;

    @Data
    public static class PlantDailyDataItem {

        private Long id;

        private LocalDate recordDate;

        private Double waterVolume;

        private Double electricity;

        private Double totalEmission;

        private Double emissionIntensity;

        private Double totalChemicals;

        private Double sludgeMass;

        private Double codRemovalRate;

        private Double tnRemovalRate;

        private Double nh3nRemovalRate;

        private Double ssRemovalRate;

        private Double tpRemovalRate;
    }
}