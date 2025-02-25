package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import lombok.Data;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Department;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Gender;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminUpdateResp {

    private Long userId;  // 用户ID
    private String username;  // 用户名
    private UserStatus status;   // 用户状态（0-禁用, 1-启用）
    private UserInfo userInfo;  // 用户信息，嵌套类
    private String roleCode;  // 角色代码，例如：SUPERVISOR、OPERATOR

    @Data
    public static class UserInfo {
        private String realName;       // 真实姓名
        private Gender gender;         // 性别
        private LocalDate birthdate; // 生日
        private String jobNumber;      // 工号
        private Department department;  // 部门
        private String position;       // 职位
        private LocalDate entryDate; // 入职时间
        private String phone;          // 电话
        private String email;          // 邮箱
        private String emergencyContact;  // 紧急联系人
        private String emergencyPhone;    // 紧急联系电话
    }
}
