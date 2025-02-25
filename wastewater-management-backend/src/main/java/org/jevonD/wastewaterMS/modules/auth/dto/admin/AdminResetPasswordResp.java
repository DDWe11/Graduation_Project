package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import lombok.Data;

@Data
public class AdminResetPasswordResp {
    private Long userId;         // 用户 ID
    private String username;     // 用户名
}
