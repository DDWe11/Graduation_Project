package org.jevonD.wastewaterMS.modules.auth.controller.user;


import org.jevonD.wastewaterMS.common.exception.PasswordMismatchException;
import org.jevonD.wastewaterMS.common.exception.SamePasswordException;
import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.jevonD.wastewaterMS.modules.auth.dto.user.*;
import org.jevonD.wastewaterMS.modules.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 获取当前用户信息
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapper<UserQueryResp>> getCurrentUser(
            @AuthenticationPrincipal UserDetails currentUser) {
        try {
            UserQueryResp resp = userService.getCurrentUserInfo(currentUser);
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body(ResponseWrapper.error(401, "未认证用户"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    // 2. 修改密码
    @PutMapping("/password")
    public ResponseEntity<ResponseWrapper<UserChangePasswordResp>> changePassword(
            @RequestBody UserChangePasswordReq req,
            @AuthenticationPrincipal UserDetails currentUser) {
        try {
            UserChangePasswordResp resp = userService.changePassword(req, currentUser);
            if (!resp.isSuccess()) {
                return ResponseEntity.badRequest()
                        .body(ResponseWrapper.error(400, resp.getErrorMessage()));
            }
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseWrapper.error(500, "密码修改失败"));
        }
    }
//@PutMapping("/password")
//public ResponseEntity<ResponseWrapper<UserChangePasswordResp>> changePassword(
//        @RequestBody UserChangePasswordReq req,
//        @AuthenticationPrincipal UserDetails currentUser) {
//    try {
//        UserChangePasswordResp resp = userService.changePassword(req, currentUser);
//        return ResponseEntity.ok(ResponseWrapper.success(resp)); // 成功返回200
//    } catch (PasswordMismatchException | SamePasswordException e) {
//        // 捕获特定的业务异常，并返回400
//        return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
//    } catch (Exception e) {
//        // 捕获其他未预期的异常，并返回500
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseWrapper.error(500, "内部服务器错误"));
//    }
//}

    @PatchMapping ("/profile")
    public ResponseEntity<ResponseWrapper<UserUpdateResp>> updateProfile(
            @RequestBody UserUpdateReq req,
            @AuthenticationPrincipal UserDetails currentUser) {
        try {
            // 安全校验：确保用户只能修改自己的信息
            UserUpdateResp resp = userService.updateUserInfo(req, currentUser);
            return ResponseEntity.ok(ResponseWrapper.success(resp));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.error(404, "用户不存在"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.error(500, "信息更新失败"));
        }
    }
}
