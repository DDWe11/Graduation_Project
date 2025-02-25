package org.jevonD.wastewaterMS.modules.auth.dto.user;

import lombok.Data;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.UserStatus;

import java.time.LocalDate;

@Data
public class UserUpdateResp {

//    private Long userId;           // 用户ID
    private String username;  // 用户名
    private UserStatus status;   // 用户状态（0-禁用, 1-启用）
    private UserInfo userInfo;  // 用户信息，嵌套类
    private String roleCode;  // 角色代码，例如：SUPERVISOR、OPERATOR

    @Data
    public static class UserInfo {
        private String realName;       // 真实姓名
        private String gender;         // 性别
        private LocalDate birthdate;
        private String phone;          // 电话
        private String email;          // 邮箱
        private String emergencyContact; // 紧急联系人
        private String emergencyPhone; // 紧急联系电话
    }
}
