package org.jevonD.wastewaterMS.modules.auth.service.impl.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import org.jevonD.wastewaterMS.modules.auth.repository.SysUserRepository;

import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 注意：这里假设用户的密码不用于认证检验，但仍必须提供非空值。
        // 权限应从用户实体中获取，这里简化为使用一个空的权限列表。
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // 确保用户实体中有密码字段，即使它不用于JWT认证
                Collections.emptyList() // 如果有角色和权限信息，应从用户实体转换为GrantedAuthority对象
        );
    }
}