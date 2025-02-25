package org.jevonD.wastewaterMS.modules.auth.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserQueryResp {

    private Long userId;          // 用户ID
    private UserInfo userInfo;

    @Data
    public static class UserInfo{
        private String username;      // 用户名
        private Integer status;       // 用户状态（0-禁用, 1-启用）
        private String roleCode;
        private String realName;      // 真实姓名
        private LocalDate birthdate;
        private String gender;        // 性别
        private String jobnumber;
        private String department;    // 部门
        private String position;      // 职位
        private LocalDate entryDate;  // 入职日期
        private String phone;         // 电话号码
        private String email;         // 邮箱
        private String emergencyContact; // 紧急联系人
        private String emergencyPhone; // 紧急联系电话
    }
}
