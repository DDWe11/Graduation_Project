package org.jevonD.wastewaterMS.modules.auth.service;

import org.jevonD.wastewaterMS.modules.auth.dto.user.*;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserQueryResp getCurrentUserInfo(UserDetails currentUser);
    UserChangePasswordResp changePassword(UserChangePasswordReq req, UserDetails currentUser);
    UserUpdateResp updateUserInfo(UserUpdateReq req,UserDetails currentUser);
}

