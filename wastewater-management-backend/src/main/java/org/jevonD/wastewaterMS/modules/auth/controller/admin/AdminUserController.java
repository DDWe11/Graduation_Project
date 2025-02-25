package org.jevonD.wastewaterMS.modules.auth.controller.admin;

import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.common.response.ResponseWrapper;
import org.jevonD.wastewaterMS.modules.auth.dto.admin.*;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.jevonD.wastewaterMS.modules.auth.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 创建用户
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<SysUser>> createUser(@RequestBody AdminCreateReq req) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(adminUserService.createUser(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<AdminDeleteResp>> deleteUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(adminUserService.deleteUser(userId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/password/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<AdminResetPasswordResp>> resetUserPassword(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ResponseWrapper.success(adminUserService.resetUserPassword(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseWrapper.error(400, e.getMessage()));
        }
    }

    /**
     * 获取用户列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<AdminQueryResp>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int size,
            @ModelAttribute AdminQueryReq queryReq) {
        return ResponseEntity.ok(ResponseWrapper.success(adminUserService.listUsers(page, size, queryReq)));
    }

    /**
     * 更新用户信息
     */
    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<AdminUpdateResp>> updateUser(@RequestBody AdminUpdateReq req) {
        return ResponseEntity.ok(ResponseWrapper.success(adminUserService.updateUser(req)));
    }
}
