package org.jevonD.wastewaterMS.modules.auth.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminCreateReq {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotNull(message = "状态不能为空")
    private Integer status;  // 0-禁用, 1-启用

    @NotBlank(message = "真实姓名不能为空")
    private String real_name;

    @NotBlank(message = "性别不能为空")
    private String gender;   // MALE / FEMALE

    @NotBlank(message = "部门不能为空")
    private String department;  // '运行部', '技术部', '环保部', '设备部', '管理层'

    @NotBlank(message = "职位不能为空")
    private String position;  // 如：生化池操作员

//    @NotNull(message = "角色ID不能为空")
//    private Long roleid;  // 表示用户角色ID
    @NotBlank(message = "角色代码不能为空")
    private String roleCode;

    @NotNull(message = "入职时间不能为空")
    private LocalDate entry_date;  // 入职时间

    @NotNull(message = "用户联系方式不能为空")
    private String phone;

    private String email;  // 可选

    private String emergencyContact;  // 可选

    private String emergencyPhone;  // 可选
}
