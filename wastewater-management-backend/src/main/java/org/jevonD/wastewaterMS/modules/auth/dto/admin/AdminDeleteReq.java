package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminDeleteReq {

    @NotNull(message = "用户ID不能为空")
    private Long userId;
}
