// SysUserInfoRepository.java
package org.jevonD.wastewaterMS.modules.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserInfoRepository extends BaseMapper<SysUserInfo> {

    // 自定义查询示例
    SysUserInfo selectByUserIdCustom(@Param("userId") Long userId);

    // 根据部门统计用户数量
    @Select("SELECT COUNT(*) FROM sys_user_info WHERE department = #{department}")
    int countByDepartment(String department);
}
