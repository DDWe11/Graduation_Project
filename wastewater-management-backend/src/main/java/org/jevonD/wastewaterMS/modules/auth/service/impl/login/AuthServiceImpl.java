package org.jevonD.wastewaterMS.modules.auth.service.impl.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jevonD.wastewaterMS.modules.auth.dto.login.LoginReq;
import org.jevonD.wastewaterMS.modules.auth.dto.login.LoginResp;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.jevonD.wastewaterMS.modules.auth.entity.SysRole;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserRole;
import org.jevonD.wastewaterMS.modules.auth.repository.SysRoleRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRoleRepository;
import org.jevonD.wastewaterMS.modules.auth.service.AuthService;
import org.jevonD.wastewaterMS.common.utils.JwtUtils;
import org.jevonD.wastewaterMS.common.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysUserRoleRepository userRoleRepository;

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

        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 在更新数据库前，将当前的 login_time 存入 last_login_time，并将 currentTime 作为新的 login_time
        userRepository.updateLoginInfo(user.getId(), currentTime, req.getLastLoginIp());

        // 返回包含 roleCode 的 LoginResp
        return LoginResp.builder()
                .token(token)
                .username(user.getUsername())
                .roleCode(role.getRoleCode())
                .loginTime(currentTime)
                .build();
    }
}