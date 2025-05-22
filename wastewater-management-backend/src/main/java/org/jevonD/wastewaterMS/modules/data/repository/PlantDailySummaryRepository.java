package org.jevonD.wastewaterMS.modules.data.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.jevonD.wastewaterMS.modules.data.entity.PlantDailySummary;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PlantDailySummaryRepository extends BaseMapper<PlantDailySummary> {
    /**
     * 多条件分页查询
     * @param page MyBatis-Plus 分页对象
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param minEmission 最小排放量
     * @param maxEmission 最大排放量
     * @return 当前页的数据列表
     */
    List<PlantDailySummary> findPageByConditions(
            Page<?> page,
            LocalDate startDate,
            LocalDate endDate,
            Double minEmission,
            Double maxEmission
    );
}