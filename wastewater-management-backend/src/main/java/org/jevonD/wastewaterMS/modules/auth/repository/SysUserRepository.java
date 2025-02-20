package org.jevonD.wastewaterMS.modules.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends BaseMapper<SysUser> {

    // 自定义方法需与 XML id 一致
    SysUser findByUsername(@Param("username") String username);
}
