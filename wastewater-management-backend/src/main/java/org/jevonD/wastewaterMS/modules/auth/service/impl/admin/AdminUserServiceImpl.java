package org.jevonD.wastewaterMS.modules.auth.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.auth.dto.admin.*;
import org.jevonD.wastewaterMS.modules.auth.entity.SysRole;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserInfo;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUserRole;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Department;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.UserStatus;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Gender;

import org.jevonD.wastewaterMS.modules.auth.repository.SysRoleRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserInfoRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRepository;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRoleRepository;
import org.jevonD.wastewaterMS.modules.auth.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl extends ServiceImpl<SysUserRepository, SysUser> implements AdminUserService {

    @Autowired
    private final SysUserRepository userRepository;

    @Autowired
    private final SysRoleRepository roleRepository;

    @Autowired
    private final SysUserRoleRepository userRoleRepository;

    @Autowired
    private final SysUserInfoRepository userInfoRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.default-password}") // 读取 application.yml 配置
    private String defaultPassword;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser createUser(AdminCreateReq req) {
        // 校验用户名唯一
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        // 查询当前最大 ID
        Long maxId = userRepository.selectMaxId();
        Long newUserId = (maxId == null) ? 1L : maxId + 1; // 确保 ID 连续递增

        // 创建用户主体
        SysUser user = new SysUser();
        user.setId(newUserId);
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setStatus(UserStatus.ENABLED);
        user.setCreateTime(LocalDateTime.now());
        userRepository.insert(user);  // 保存用户主体

        // 创建用户信息
        SysUserInfo userInfo = new SysUserInfo();
        userInfo.setUserId(newUserId);
        userInfo.setRealName(req.getReal_name());
        userInfo.setGender(Gender.fromCode(req.getGender()));   // 转换为 Gender 枚举
        userInfo.setDepartment(Department.valueOf(req.getDepartment().toUpperCase()));  // 转换为 Department 枚举
        userInfo.setPosition(req.getPosition());
        userInfo.setEntryDate(req.getEntry_date());
        userInfo.setJobNumber(generateJobNumber(req.getDepartment()));  // 生成工号
        userInfoRepository.insert(userInfo);  // 保存用户信息

        Long roleId = req.getRoleid();
        // 分配角色
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(newUserId);
        userRole.setRoleId(roleId);
        userRoleRepository.insert(userRole);  // 保存用户角色
        return user;
    }

    private String generateJobNumber(String department) {
        // 生成规则：部门代码+年月+序号（示例：YXB-202311-001）
        String prefix = department.substring(0, 3).toUpperCase();
        String datePart = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));
        String sequence = String.format("%03d", userInfoRepository.countByDepartment(department) + 1);
        return prefix + "-" + datePart + "-" + sequence;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminDeleteResp deleteUser(Long userId) {
        // 查询用户信息
        SysUser user = userRepository.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 查询用户角色
        SysUserRole userRole = userRoleRepository.selectOne(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        if (userRole == null) {
            throw new IllegalArgumentException("用户角色信息缺失");
        }

        SysRole role = roleRepository.selectById(userRole.getRoleId());
        if (role == null) {
            throw new IllegalArgumentException("无法获取用户角色信息");
        }

        // 只允许删除 SUPERVISOR/OPERATOR 角色的用户
        if (!role.getRoleCode().equals("SUPERVISOR") && !role.getRoleCode().equals("OPERATOR")) {
            throw new IllegalArgumentException("无法删除管理员用户");
        }

        // 删除用户相关表数据
        userRoleRepository.delete(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        userInfoRepository.delete(new QueryWrapper<SysUserInfo>().eq("user_id", userId));
        userRepository.deleteById(userId);

        // 构造返回信息
        AdminDeleteResp resp = new AdminDeleteResp();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRoleCode(role.getRoleCode());

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRoles(Long userId, String roleCode) {
        SysRole role = roleRepository.selectOne(new QueryWrapper<SysRole>().eq("role_code", roleCode));
        if (role == null) {
            throw new IllegalArgumentException("角色 " + roleCode + " 不存在");
        }
        SysUserRole existingUserRole = userRoleRepository.selectOne(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        if (existingUserRole != null) {
            existingUserRole.setRoleId(Long.valueOf(role.getId()));
            userRoleRepository.updateById(existingUserRole);
        } else {
            SysUserRole newUserRole = new SysUserRole();
            newUserRole.setUserId(userId);
            newUserRole.setRoleId(Long.valueOf(role.getId()));
            userRoleRepository.insert(newUserRole);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminResetPasswordResp resetUserPassword(Long userId) {
        SysUser user = userRepository.selectById(userId);

        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 确保不能重置 ADMIN 账号
        SysUserRole userRole = userRoleRepository.selectOne(
                new QueryWrapper<SysUserRole>().eq("user_id", userId)
        );

        SysRole role = roleRepository.selectById(userRole.getRoleId());
        if (role != null && "ADMIN".equals(role.getRoleCode())) {
            throw new IllegalArgumentException("无法重置 ADMIN 用户密码");
        }

        // 设定默认密码
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        // 更新密码
        user.setPassword(encodedPassword);
        userRepository.updateById(user);

        // 构建返回值
        AdminResetPasswordResp resp = new AdminResetPasswordResp();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        return resp;
    }

    @Override
    public AdminQueryResp listUsers(int page, int size, AdminQueryReq queryReq) {
        Page<SysUser> pageInfo = new Page<>(page, size);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        // 按 username 筛选
        if (queryReq.getUsername() != null && !queryReq.getUsername().isEmpty()) {
            wrapper.eq("username", queryReq.getUsername());
        }

        // 按状态筛选
        if (queryReq.getStatus() != null) {
            wrapper.eq("status", queryReq.getStatus());
        }

        // 按部门筛选（需要关联 sys_user_info）
        if (queryReq.getDepartment() != null && !queryReq.getDepartment().isEmpty()) {
            wrapper.inSql("id", "SELECT user_id FROM sys_user_info WHERE department = '" + queryReq.getDepartment() + "'");
        }

        // 按角色筛选（需要关联 sys_user_role）
        if (queryReq.getRoleCode() != null && !queryReq.getRoleCode().isEmpty()) {
            wrapper.inSql("id", "SELECT user_id FROM sys_user_role ur JOIN sys_role r ON ur.role_id = r.id WHERE r.role_code = '" + queryReq.getRoleCode() + "'");
        }

        Page<SysUser> userPage = userRepository.selectPage(pageInfo, wrapper);

        // 构造返回结果
        AdminQueryResp response = new AdminQueryResp();
        response.setTotal((int) userPage.getTotal());
        List<AdminQueryResp.UserInfo> userInfos = userPage.getRecords().stream().map(user -> {
            SysUserInfo userInfo = userInfoRepository.selectById(user.getId());
            SysUserRole userRole = userRoleRepository.selectOne(new QueryWrapper<SysUserRole>().eq("user_id", user.getId()));
            SysRole role = roleRepository.selectById(userRole.getRoleId());

            AdminQueryResp.UserInfo info = new AdminQueryResp.UserInfo();
            info.setUserId(user.getId());
            info.setUsername(user.getUsername());
            info.setRealName(userInfo.getRealName());
            info.setGender(userInfo.getGender().name());
            info.setDepartment(userInfo.getDepartment().name());
            info.setPosition(userInfo.getPosition());
            info.setRoleCode(role.getRoleCode());
            info.setStatus(user.getStatus().ordinal());
            info.setJobNumber(userInfo.getJobNumber());
            info.setPhone(userInfo.getPhone());
            info.setEmail(userInfo.getEmail());
            info.setEmergencyContact(userInfo.getEmergencyContact());
            info.setEmergencyPhone(userInfo.getEmergencyPhone());
            info.setEntryDate(userInfo.getEntryDate());
            return info;
        }).collect(Collectors.toList());
        response.setUsers(userInfos);
        return response;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminUpdateResp updateUser(AdminUpdateReq req) {
        SysUser user = userRepository.selectById(req.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 更新用户基本信息
        if (req.getUsername() != null) user.setUsername(req.getUsername());
        if (req.getStatus() != null) user.setStatus(UserStatus.values()[req.getStatus()]);
        userRepository.updateById(user);

        // 更新用户详细信息
        SysUserInfo userInfo = userInfoRepository.selectById(req.getUserId());
        if (userInfo != null) {
            if (req.getReal_name() != null) userInfo.setRealName(req.getReal_name());
            if (req.getGender() != null) userInfo.setGender(Gender.valueOf(req.getGender().toUpperCase()));
            if (req.getBirthdate() != null) userInfo.setBirthdate(req.getBirthdate());
            if (req.getDepartment() != null) userInfo.setDepartment(Department.valueOf(req.getDepartment().toUpperCase()));
            if (req.getPosition() != null) userInfo.setPosition(req.getPosition());
            if (req.getEntry_date() != null) userInfo.setEntryDate(req.getEntry_date());
            if (req.getPhone() != null) userInfo.setPhone(req.getPhone());
            if (req.getEmail() != null) userInfo.setEmail(req.getEmail());
            if (req.getEmergencyContact() != null) userInfo.setEmergencyContact(req.getEmergencyContact());
            if (req.getEmergencyPhone() != null) userInfo.setEmergencyPhone(req.getEmergencyPhone());

            userInfoRepository.updateById(userInfo);
            if (req.getRoleCode() != null) {
                updateUserRoles(req.getUserId(), req.getRoleCode());
            }
        }

        // **更新角色信息**
        String roleCode = null;
        if (req.getRoleCode() != null) {
            // 查找 `sys_role` 表，获取 `roleId`
            SysRole role = roleRepository.selectOne(new QueryWrapper<SysRole>().eq("role_code", req.getRoleCode()));
            if (role == null) {
                throw new IllegalArgumentException("角色代码无效: " + req.getRoleCode());
            }

            // 在 `sys_user_role` 表中查找用户当前的角色
            SysUserRole userRole = userRoleRepository.selectOne(new QueryWrapper<SysUserRole>().eq("user_id", req.getUserId()));

            if (userRole != null) {
                // **如果已有角色，执行更新**
                userRole.setRoleId(Long.valueOf(role.getId()));
                userRoleRepository.updateById(userRole);
            } else {
                // **如果没有角色，执行插入**
                userRole = new SysUserRole();
                userRole.setUserId(req.getUserId());
                userRole.setRoleId(Long.valueOf(role.getId()));
                userRoleRepository.insert(userRole);
            }
            roleCode = req.getRoleCode(); // 赋值新的 roleCode
        } else {
            // **如果 `roleCode` 没有传递，则查询当前用户角色**
            SysUserRole userRole = userRoleRepository.selectOne(new QueryWrapper<SysUserRole>().eq("user_id", req.getUserId()));
            if (userRole != null) {
                SysRole existingRole = roleRepository.selectById(userRole.getRoleId());
                if (existingRole != null) {
                    roleCode = existingRole.getRoleCode();
                }
            }
        }

        // **封装返回数据**
        AdminUpdateResp resp = new AdminUpdateResp();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setStatus(user.getStatus());
        resp.setRoleCode(roleCode);

        AdminUpdateResp.UserInfo userInfoResp = new AdminUpdateResp.UserInfo();
        userInfoResp.setRealName(userInfo.getRealName());
        userInfoResp.setGender(userInfo.getGender());

        // **避免 birthdate 为空时报错**
        if (userInfo.getBirthdate() != null) {
            userInfoResp.setBirthdate(userInfo.getBirthdate());
        }

        userInfoResp.setJobNumber(userInfo.getJobNumber());
        userInfoResp.setDepartment(userInfo.getDepartment());
        userInfoResp.setPosition(userInfo.getPosition());

        // **避免 entryDate 为空时报错**
        if (userInfo.getEntryDate() != null) {
            userInfoResp.setEntryDate(userInfo.getEntryDate());
        }

        userInfoResp.setPhone(userInfo.getPhone());
        userInfoResp.setEmail(userInfo.getEmail());
        userInfoResp.setEmergencyContact(userInfo.getEmergencyContact());
        userInfoResp.setEmergencyPhone(userInfo.getEmergencyPhone());

        resp.setUserInfo(userInfoResp);

        return resp;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long userId, boolean enabled) {
        SysUser user = userRepository.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        user.setStatus(enabled ? UserStatus.ENABLED : UserStatus.DISABLED);
        userRepository.updateById(user);
    }
}
