package org.jevonD.wastewaterMS.modules.auth.dto.login;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class LoginReq {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String lastLoginIp;
}
