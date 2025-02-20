package org.jevonD.wastewaterMS.modules.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRoleRepository extends BaseMapper<SysUserRole> {
    // 可根据需要自定义查询方法
    // 根据联合主键查询
    SysUserRole selectByCompositeKey(
            @Param("userId") Long userId,
            @Param("roleId") Integer roleId
    );
}

