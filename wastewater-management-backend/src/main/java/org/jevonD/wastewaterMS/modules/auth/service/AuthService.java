package org.jevonD.wastewaterMS.modules.auth.service;

import org.jevonD.wastewaterMS.modules.auth.dto.login.LoginReq;
import org.jevonD.wastewaterMS.modules.auth.dto.login.LoginResp;

public interface AuthService {
    LoginResp login(LoginReq req);
}
