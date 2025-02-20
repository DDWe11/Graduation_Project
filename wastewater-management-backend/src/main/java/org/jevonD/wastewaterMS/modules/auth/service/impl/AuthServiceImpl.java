package org.jevonD.wastewaterMS.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jevonD.wastewaterMS.modules.auth.dto.LoginReq;
import org.jevonD.wastewaterMS.modules.auth.dto.LoginResp;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.jevonD.wastewaterMS.modules.auth.entity.SysRole;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserRole;
import org.jevonD.wastewaterMS.modules.auth.repository.SysRoleRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRoleRepository;  // 引入sys_user_role表的repository
import org.jevonD.wastewaterMS.modules.auth.service.AuthService;
import org.jevonD.wastewaterMS.common.utils.JwtUtils;
import org.jevonD.wastewaterMS.common.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysUserRoleRepository userRoleRepository;  // 引入sys_user_role表的repository

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginResp login(LoginReq req) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", req.getUsername());
        SysUser user = userRepository.selectOne(queryWrapper);

        if (user == null) {
            throw new AuthException("用户不存在");
        }

        // 校验密码
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new AuthException("密码错误");
        }

        // 获取角色信息
        QueryWrapper<SysUserRole> userRoleQuery = new QueryWrapper<>();
        userRoleQuery.eq("user_id", user.getId());
        SysUserRole userRole = userRoleRepository.selectOne(userRoleQuery);

        if (userRole == null) {
            throw new AuthException("用户角色不存在");
        }

        // 获取角色名称
        SysRole role = roleRepository.selectById(userRole.getRoleId());
        if (role == null) {
            throw new AuthException("角色信息不存在");
        }

        // 生成 JWT Token
        String token = jwtUtils.generateToken(user.getUsername());

        // 返回包含 roleCode 的 LoginResp
        return LoginResp.builder()
                .token(token)
                .username(user.getUsername())
                .roleCode(role.getRoleCode()) // 添加 roleCode
                .build();
    }
}