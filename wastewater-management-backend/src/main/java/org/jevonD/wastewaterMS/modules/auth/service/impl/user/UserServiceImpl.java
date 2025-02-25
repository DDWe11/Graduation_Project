package org.jevonD.wastewaterMS.modules.auth.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jevonD.wastewaterMS.modules.auth.entity.SysRole;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserInfo;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserRole;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Gender;
import org.jevonD.wastewaterMS.modules.auth.repository.SysRoleRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.jevonD.wastewaterMS.modules.auth.dto.user.*;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserInfoRepository;
import org.jevonD.wastewaterMS.modules.auth.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysUserInfoRepository userInfoRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysUserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserQueryResp getCurrentUserInfo(UserDetails currentUser) {
        // 1. 获取当前登录用户基本信息
        String username = currentUser.getUsername();
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        // 2. 获取用户详细信息
        SysUserInfo userInfo = userInfoRepository.selectById(user.getId());
        if (userInfo == null) {
            throw new RuntimeException("用户详细信息未配置");
        }

        // 3. 获取用户角色编码
        String roleCode = getRoleCodeByUserId(user.getId());

        // 4. 构建响应对象
        return buildUserQueryResp(user, userInfo, roleCode);
    }

    private String getRoleCodeByUserId(Long userId) {
        // 查询用户角色关联
        SysUserRole userRole = userRoleRepository.selectOne(
                new QueryWrapper<SysUserRole>().eq("user_id", userId)
        );

        if (userRole == null) {
            return "N/A"; // 或根据业务需求抛出异常
        }

        // 查询角色信息
        SysRole role = roleRepository.selectById(userRole.getRoleId());
        return (role != null) ? role.getRoleCode() : "N/A";
    }

    private UserQueryResp buildUserQueryResp(SysUser user, SysUserInfo userInfo, String roleCode) {
        UserQueryResp resp = new UserQueryResp();
        resp.setUserId(user.getId());

        UserQueryResp.UserInfo info = new UserQueryResp.UserInfo();
        // 基本信息
        info.setUsername(user.getUsername());
        info.setStatus(user.getStatus().ordinal()); // 假设 UserStatus 是枚举

        // 详细信息
        info.setRealName(userInfo.getRealName());
        info.setBirthdate(userInfo.getBirthdate()); // 日期格式化
        info.setGender(userInfo.getGender().name());
        info.setJobnumber(userInfo.getJobNumber());
        info.setDepartment(userInfo.getDepartment().name());
        info.setPosition(userInfo.getPosition());
        info.setEntryDate(userInfo.getEntryDate());
        info.setPhone(userInfo.getPhone());
        info.setEmail(userInfo.getEmail());
        info.setEmergencyContact(userInfo.getEmergencyContact());
        info.setEmergencyPhone(userInfo.getEmergencyPhone());

        // 角色信息
        info.setRoleCode(roleCode);

        resp.setUserInfo(info);
        return resp;
    }

    @Override
    @Transactional
    public UserChangePasswordResp changePassword(UserChangePasswordReq req, UserDetails currentUser) {
        String username = currentUser.getUsername();
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            return buildErrorResp("PASSWORD_MISMATCH", "旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), user.getPassword())) {
            return buildErrorResp("PASSWORD_SAME", "新密码不能与旧密码相同");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.updateById(user);

        return UserChangePasswordResp.builder()
                .success(true)
                .errorCode(null)
                .errorMessage(null)
                .timestamp(LocalDateTime.now()) // 确保时间戳正确生成
                .build();
    }

    private UserChangePasswordResp buildErrorResp(String errorCode, String message) {
        return UserChangePasswordResp.builder()
                .success(false)
                .errorCode("400")
                .errorMessage(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserUpdateResp updateUserInfo(UserUpdateReq req, UserDetails currentUser) {
        // 使用用户名查找用户
        String username = currentUser.getUsername();
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        // 使用获取到的用户ID查找用户详细信息
        SysUserInfo userInfo = userInfoRepository.selectById(user.getId());
        if (userInfo == null) {
            throw new IllegalArgumentException("用户详细信息不存在");
        }

        // 使用用户ID查找用户角色
        SysUserRole userRole = userRoleRepository.selectOne(new QueryWrapper<SysUserRole>().eq("user_id", user.getId()));
        SysRole role = userRole != null ? roleRepository.selectById(userRole.getRoleId()) : null;

        // 更新用户详细信息
        updateUserInfoDetails(req, userInfo);
        userInfoRepository.updateById(userInfo);

        // 构建返回的响应对象
        return buildUserUpdateResp(user, userInfo, role);
    }

    private void updateUserInfoDetails(UserUpdateReq req, SysUserInfo userInfo) {
        if (req.getReal_name() != null) userInfo.setRealName(req.getReal_name());
        if (req.getGender() != null) userInfo.setGender(Gender.valueOf(req.getGender().toUpperCase()));
        if (req.getBirthdate() != null) userInfo.setBirthdate(req.getBirthdate());
        if (req.getPhone() != null) userInfo.setPhone(req.getPhone());
        if (req.getEmail() != null) userInfo.setEmail(req.getEmail());
        if (req.getEmergencyContact() != null) userInfo.setEmergencyContact(req.getEmergencyContact());
        if (req.getEmergencyPhone() != null) userInfo.setEmergencyPhone(req.getEmergencyPhone());
    }

    private UserUpdateResp buildUserUpdateResp(SysUser user, SysUserInfo userInfo, SysRole role) {
        UserUpdateResp resp = new UserUpdateResp();
        resp.setUsername(user.getUsername());
        resp.setRoleCode(role != null ? role.getRoleCode() : "N/A");
        resp.setStatus(user.getStatus());

        UserUpdateResp.UserInfo userInfoResp = new UserUpdateResp.UserInfo();
        userInfoResp.setRealName(userInfo.getRealName());
        userInfoResp.setGender(userInfo.getGender().name());
        userInfoResp.setBirthdate(userInfo.getBirthdate());
        userInfoResp.setPhone(userInfo.getPhone());
        userInfoResp.setEmail(userInfo.getEmail());
        userInfoResp.setEmergencyContact(userInfo.getEmergencyContact());
        userInfoResp.setEmergencyPhone(userInfo.getEmergencyPhone());

        resp.setUserInfo(userInfoResp);

        return resp;
    }
}