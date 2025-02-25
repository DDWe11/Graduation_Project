package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AdminQueryResp {

    private Integer total;  // 总记录数
    private List<UserInfo> users;  // 用户列表

    @Data
    public static class UserInfo {
        private Long userId;        // 用户ID
        private String username;    // 用户名
        private String realName;    // 真实姓名
        private String gender;      // 性别 ('M' - 男, 'F' - 女)
        private String department;  // 部门
        private String position;    // 职位
        private String roleCode;   // 角色代码（例如：SUPERVISOR、OPERATOR）
        private Integer status;     // 用户状态（0-禁用, 1-启用）

        // 用户详细信息
        private String jobNumber;   // 工号
        private String phone;       // 电话
        private String email;       // 邮箱
        private String emergencyContact; // 紧急联系人
        private String emergencyPhone;   // 紧急联系人电话
        private LocalDate entryDate;     // 入职时间

    }
}
