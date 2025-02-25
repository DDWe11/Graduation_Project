package org.jevonD.wastewaterMS.modules.auth.dto.login;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginResp {
    private String token;
    private String username;
    private String roleCode;
    private LocalDateTime loginTime;
}
