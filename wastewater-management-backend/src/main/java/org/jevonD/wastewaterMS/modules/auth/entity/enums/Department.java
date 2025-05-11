package org.jevonD.wastewaterMS.modules.auth.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Department {
    运营部("OPERATIONS", "OP"),
    技术部("TECHNOLOGY", "TECH"),
    安全环保部("SAFETY&ENVIRONMENT", "SE"),
    设备维护部("MAINTENANCE", "MT"),
    行政与后勤部("ADMINISTRATION&LOGISTICS", "AL"),
    监测部("MONITORING", "MON");

    @EnumValue
    private final String chineseName;

    private final String englishName;
    private final String shortCode; // 部门简写

    Department(String englishName, String shortCode) {
        this.chineseName = this.name(); // 直接使用中文枚举名
        this.englishName = englishName;
        this.shortCode = shortCode;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public static Department fromChinese(String chineseName) {
        for (Department dept : Department.values()) {
            if (dept.getChineseName().equals(chineseName)) {
                return dept;
            }
        }
        throw new IllegalArgumentException("Invalid department name: " + chineseName);
    }
}
