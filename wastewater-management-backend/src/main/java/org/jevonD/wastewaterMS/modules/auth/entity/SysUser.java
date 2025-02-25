package org.jevonD.wastewaterMS.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.UserStatus;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")  // 映射数据库表
public class SysUser {

    @TableId(type = IdType.INPUT)  // 自动生成主键
    private Long id;

    private String username;
    private String password;

    @EnumValue  // 标明如何存储枚举值到数据库
    private UserStatus status;  // 启用/禁用状态

    private String lastLoginIp;  // 最后登录 IP

    private LocalDateTime lastLoginTime;  // 最后登录时间
    private LocalDateTime createTime;
    private LocalDateTime loginTime;  // 当前登录时间
}
