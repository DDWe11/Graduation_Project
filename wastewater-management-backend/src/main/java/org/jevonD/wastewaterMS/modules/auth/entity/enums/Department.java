package org.jevonD.wastewaterMS.modules.auth.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Department {
    运行部("RUNNING"),
    技术部("TECHNOLOGY"),
    环保部("ENVIRONMENTAL"),
    设备部("EQUIPMENT"),
    管理层("MANAGEMENT");

    @EnumValue  // 存储数据库实际值（中文）
    private final String chineseName;

    // 附加英文标识（非必须）
    private final String englishCode;

    Department(String englishCode) {
        this.chineseName = this.name(); // 枚举名称即中文值
        this.englishCode = englishCode;
    }

    public String getChineseName() {
        return chineseName;
    }
}
