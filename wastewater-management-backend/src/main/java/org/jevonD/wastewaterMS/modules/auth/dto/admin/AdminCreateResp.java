package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import lombok.Data;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Department;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Gender;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.UserStatus;

import java.time.LocalDateTime;

@Data
public class AdminCreateResp {

    private String username;  // 用户名
    private String password;  // 密码
    private UserStatus status;   // 用户状态（0-禁用, 1-启用）
    private UserInfo userInfo;  // 用户信息，嵌套类
    @Data
    public static class UserInfo {
        private String realName;       // 真实姓名
        private Gender gender;
        private String job_number;
        private Department department;     // 部门
        private String position;
        private String roleCode;  // 角色代码，例如：SUPERVISOR
        private String phone;                // 手机号（可选）
        private String email;                // 邮箱（可选）
        private String emergencyContact;     // 紧急联系人（可选）
        private String emergencyPhone;       // 紧急联系电话（可选）
        private LocalDateTime entry_time;
    }
}
