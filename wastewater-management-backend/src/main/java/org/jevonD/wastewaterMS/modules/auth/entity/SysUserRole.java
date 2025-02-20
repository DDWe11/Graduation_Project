package org.jevonD.wastewaterMS.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class SysUserRole {

    @TableId
    private Long userId;
    private Long roleId;
}
