package org.jevonD.wastewaterMS.modules.auth.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserChangePasswordResp {
    private boolean success;      // 操作是否成功
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;     // 错误编码（如 PASSWORD_MISMATCH）
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private String errorMessage;  // 错误详情（如"旧密码错误"）
    private LocalDateTime timestamp = LocalDateTime.now();
}
