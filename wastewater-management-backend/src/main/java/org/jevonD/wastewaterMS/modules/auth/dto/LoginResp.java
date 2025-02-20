package org.jevonD.wastewaterMS.modules.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResp {
    private String token;
    private String username;
    private String roleCode;
}
