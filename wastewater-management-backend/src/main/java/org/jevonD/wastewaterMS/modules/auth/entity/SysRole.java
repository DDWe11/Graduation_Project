package org.jevonD.wastewaterMS.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.RoleStatus;

@Data
@TableName("sys_role")  // 映射到数据库中的 sys_role 表
public class SysRole {

    @TableId(type = IdType.AUTO)
    private Integer id;  // 角色ID

    private String roleCode;  // 角色编码
    private String roleName;  // 角色名称
    private String description;  // 角色描述
    @EnumValue
    private RoleStatus status;  // 使用 RoleStatus 枚举类型
}
