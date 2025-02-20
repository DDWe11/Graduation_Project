package org.jevonD.wastewaterMS.modules.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jevonD.wastewaterMS.modules.auth.entity.SysRole;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository extends BaseMapper<SysRole> {
    // 可以增加一些自定义查询方法
}
