package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminUpdateReq {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    // 用户基础信息表 - sys_user
    private String username;  // 可选：用户名
    private Integer status;   // 可选：用户状态（0-禁用, 1-启用）

    // 用户详细信息表 - sys_user_info
    private String real_name;  // 可选：真实姓名
    private String gender;     // 可选：性别 ('M' - 男, 'F' - 女)
    private LocalDate birthdate;
    private String job_number; // 可选：工号
    private String department; // 可选：部门
    private String position;   // 可选：职位
    private LocalDate entry_date;  // 可选：入职时间
    private String phone;
    private String email;
    private String emergencyContact;
    private String emergencyPhone;

    // 用户角色信息表 - sys_user_role
    private String roleCode;  // 可选：角色代码，例如：SUPERVISOR、OPERATOR
}
