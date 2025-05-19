package org.jevonD.wastewaterMS.modules.process.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jevonD.wastewaterMS.modules.process.entity.ProcessUnit;

@Mapper
public interface ProcessUnitRepository extends BaseMapper<ProcessUnit> {
    // 可添加按名称、顺序查找等方法
}
