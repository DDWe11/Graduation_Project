package org.jevonD.wastewaterMS.modules.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jevonD.wastewaterMS.modules.auth.entity.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysRoleRepository extends BaseMapper<SysRole> {
    // 根据用户ID查询角色列表
    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<SysRole> findRolesByUserId(@Param("userId") Long userId);
}
