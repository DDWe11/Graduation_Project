package org.jevonD.wastewaterMS.modules.auth.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Gender {
    MALE("M"),
    FEMALE("F");

    @EnumValue
    private final String code;

    Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    // 根据存储的 code 获取 Gender 枚举常量
    public static Gender fromCode(String code) {
        for (Gender gender : Gender.values()) {
            if (gender.getCode().equalsIgnoreCase(code)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender code: " + code);
    }
}
