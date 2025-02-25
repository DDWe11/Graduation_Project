package org.jevonD.wastewaterMS.modules.auth.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserStatus {
    DISABLED(0), ENABLED(1);

    @EnumValue  // 注明如何存储到数据库
    private final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserStatus fromValue(int value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}