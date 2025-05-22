package org.jevonD.wastewaterMS.modules.data.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jevonD.wastewaterMS.modules.data.entity.PlantDrugUsage;

import java.time.LocalDate;
import java.util.List;

public interface PlantDrugUsageRepository {
    /**
     * 分页+条件筛选
     */
    List<PlantDrugUsage> findPageByConditions(
            Page<?> page,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}