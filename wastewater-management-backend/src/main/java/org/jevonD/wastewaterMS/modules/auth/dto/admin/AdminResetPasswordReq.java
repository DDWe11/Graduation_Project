package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminResetPasswordReq {

    @NotBlank(message = "用户ID不能为空")
    private Long userId;  // 用户ID

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;  // 旧密码

    @NotBlank(message = "新密码不能为空")
    private String newPassword;  // 新密码
}