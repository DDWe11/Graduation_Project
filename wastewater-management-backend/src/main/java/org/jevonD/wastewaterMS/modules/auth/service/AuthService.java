package org.jevonD.wastewaterMS.modules.auth.service;

import org.jevonD.wastewaterMS.modules.auth.dto.LoginReq;
import org.jevonD.wastewaterMS.modules.auth.dto.LoginResp;

public interface AuthService {
    LoginResp login(LoginReq req);
}
