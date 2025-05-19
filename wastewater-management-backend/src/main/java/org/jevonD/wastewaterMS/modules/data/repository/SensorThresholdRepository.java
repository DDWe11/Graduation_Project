package org.jevonD.wastewaterMS.modules.data.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jevonD.wastewaterMS.modules.data.entity.SensorThreshold;

@Mapper
public interface SensorThresholdRepository extends BaseMapper<SensorThreshold> {
    // 根据sensorId查询启用状态的阈值配置
    SensorThreshold findBySensorIdAndEnabled(@Param("sensorId") Long sensorId, @Param("enabled") Integer enabled);
}
