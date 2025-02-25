package org.jevonD.wastewaterMS.modules.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SysUserRepository extends BaseMapper<SysUser> {
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    Optional<SysUser> findByUsername(@Param("username") String username);

    // 更新登录信息（XML方式更推荐）
    int updateLoginInfo(@Param("userId") Long userId,
                        @Param("lastLoginTime") LocalDateTime lastLoginTime,
                        @Param("lastLoginIp") String lastLoginIp);

    //保证userid连续递增
    @Select("SELECT COALESCE(MAX(id), 0) FROM sys_user FOR UPDATE ")
    Long selectMaxId();

}


