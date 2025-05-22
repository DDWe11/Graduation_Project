package org.jevonD.wastewaterMS.modules.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("plant_water_quality")
public class PlantWaterQuality {
    @TableId(type = IdType.AUTO)
    private Long id;

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