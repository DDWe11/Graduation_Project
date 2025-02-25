package org.jevonD.wastewaterMS.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Department;
import org.jevonD.wastewaterMS.modules.auth.entity.enums.Gender;

import java.time.LocalDate;

@Data
@TableName("sys_user_info")
public class SysUserInfo {
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    // 真实姓名
    private String realName;

    @EnumValue
    private Gender gender;

    // 出生日期
    private LocalDate birthdate;

    // 工号（唯一）
    private String jobNumber;

    @EnumValue
    private Department department;

    // 职位
    private String position;

    // 入职日期（数据库字段entry_date）
    @TableField("entry_date")
    private LocalDate entryDate;

    // 联系电话
    private String phone;

    // 邮箱
    private String email;

    // 紧急联系人
    private String emergencyContact;

    // 紧急联系电话
    private String emergencyPhone;
}
