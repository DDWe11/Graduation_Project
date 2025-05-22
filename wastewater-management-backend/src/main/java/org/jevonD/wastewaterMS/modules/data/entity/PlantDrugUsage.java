package org.jevonD.wastewaterMS.modules.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("plant_drug_usage")
public class PlantDrugUsage {
    @TableId(type = IdType.AUTO)
    private Long id;

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