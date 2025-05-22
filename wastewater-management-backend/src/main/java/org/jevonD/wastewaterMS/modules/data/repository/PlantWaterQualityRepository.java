package org.jevonD.wastewaterMS.modules.data.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jevonD.wastewaterMS.modules.data.entity.PlantWaterQuality;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PlantWaterQualityRepository extends BaseMapper<PlantWaterQuality> {
    /**
     * 按日期分页查询水质水量明细
     */
    List<PlantWaterQuality> findPageByConditions(
            Page<?> page,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}