package org.jevonD.wastewaterMS.modules.auth.controller;

import org.jevonD.wastewaterMS.modules.auth.dto.LoginReq;
import org.jevonD.wastewaterMS.modules.auth.dto.LoginResp;
import org.jevonD.wastewaterMS.modules.auth.service.AuthService;
import org.jevonD.wastewaterMS.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseResult<LoginResp> login(@RequestBody @Valid LoginReq req) {
        LoginResp loginResp = authService.login(req);
        return new ResponseResult<>(200, "登录成功", loginResp);
    }
}
