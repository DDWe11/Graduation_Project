package org.jevonD.wastewaterMS.modules.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("plant_daily_summary")
public class PlantDailySummary {

    @TableId(type = IdType.AUTO)
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