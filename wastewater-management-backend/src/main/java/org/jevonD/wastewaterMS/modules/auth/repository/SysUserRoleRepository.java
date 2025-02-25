package org.jevonD.wastewaterMS.modules.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends BaseMapper<SysUserRole> {
    // 可根据需要自定义查询方法
    // 根据联合主键查询
    SysUserRole selectByCompositeKey(
            @Param("userId") Long userId,
            @Param("roleId") Integer roleId
    );
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId} AND role_id IN (#{roleIds})")
    int deleteUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Integer> roleIds);


}

