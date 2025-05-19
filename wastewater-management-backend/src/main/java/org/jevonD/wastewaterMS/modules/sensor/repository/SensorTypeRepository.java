package org.jevonD.wastewaterMS.modules.sensor.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jevonD.wastewaterMS.modules.sensor.entity.SensorType;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SensorTypeRepository extends BaseMapper<SensorType> {
    @Select("SELECT * FROM sensor_type WHERE code = #{code} LIMIT 1")
    SensorType findByCode(String code);

    @Select("SELECT * FROM sensor_type WHERE name = #{name} LIMIT 1")
    SensorType findByName(String name);

    default List<SensorType> findByConditions(String code, String name) {
        QueryWrapper<SensorType> wrapper = new QueryWrapper<>();

        if (code != null && !code.isEmpty()) {
            wrapper.eq("code", code);
        }
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }

        return selectList(wrapper);
    }

    //保证userid连续递增
    @Select("SELECT COALESCE(MAX(id), 0) FROM sensor_type FOR UPDATE ")
    Long selectMaxId();

    default List<SensorType> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return selectBatchIds(ids);
    }
}
