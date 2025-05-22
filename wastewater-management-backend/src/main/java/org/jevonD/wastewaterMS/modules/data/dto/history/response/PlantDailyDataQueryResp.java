package org.jevonD.wastewaterMS.modules.data.dto.history.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlantDailySummaryResp {

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