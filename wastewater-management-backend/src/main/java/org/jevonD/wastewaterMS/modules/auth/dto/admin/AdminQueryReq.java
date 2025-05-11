package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import lombok.Data;

@Data
public class AdminQueryReq {
    private String username;    // 用户名（sys_user.username）
    private Integer status;     // 用户状态（0-禁用, 1-启用）
    private String department;  // 部门（sys_user_info.department）
    private String roleCode;    // 角色代码（sys_role.role_code）
    private String phone;       // 手机号（sys_user_info.phone）
    private String realName;    // 真实姓名（sys_user_info.real_name）

    private Integer page = 1;
    private Integer size = 10;
}
