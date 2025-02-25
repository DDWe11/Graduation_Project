package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import lombok.Data;

@Data
public class AdminDeleteResp {

    private Long userId;  // 被删除的用户ID
    private String username;
    private String roleCode;
}
