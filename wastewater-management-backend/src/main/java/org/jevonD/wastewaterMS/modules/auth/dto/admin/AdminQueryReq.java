package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import lombok.Data;

@Data
public class AdminQueryReq {

    private String username;  // 用户名（可选，用于过滤）
    private Integer status;   // 用户状态（0-禁用, 1-启用）（可选）
    private String department; // 部门
    private String roleCode;  // 角色代码（可选）
    private Integer page = 1; // 分页页码
    private Integer size = 10; // 每页条数
}
