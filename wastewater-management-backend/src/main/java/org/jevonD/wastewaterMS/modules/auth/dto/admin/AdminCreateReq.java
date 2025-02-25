package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminCreateReq {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotNull(message = "状态不能为空")
    private Integer status;  // 0-禁用, 1-启用

    @NotBlank(message = "真实姓名不能为空")
    private String real_name;

    @NotBlank(message = "性别不能为空")
    private String gender;   // 'M' - 男, 'F' - 女

    @NotBlank(message = "部门不能为空")
    private String department;  // '运行部', '技术部', '环保部', '设备部', '管理层'

    @NotBlank(message = "职位不能为空")
    private String position;  // 如：生化池操作员

    @NotNull(message = "角色ID不能为空")
    private Long roleid;  // 新增字段，表示用户角色ID

    @NotNull(message = "入职时间不能为空")
    private LocalDate entry_date;  // 入职时间字段，类型为 LocalDate，映射数据库中的 DATE 类型
}
