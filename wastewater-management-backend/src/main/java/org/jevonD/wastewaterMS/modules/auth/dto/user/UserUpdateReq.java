package org.jevonD.wastewaterMS.modules.auth.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateReq {

//    @NotNull(message = "用户ID不能为空")
//    private Long userId;   // 用户ID，必填字段

    private String real_name;       // 真实姓名，可选
    private String gender;         // 性别 'M' - 男, 'F' - 女，可选
    private LocalDate birthdate;    // 出生日期，可选
    private String phone;          // 电话，可选
    private String email;          // 邮箱，可选
    private String emergencyContact; // 紧急联系人，可选
    private String emergencyPhone; // 紧急联系电话，可选
}