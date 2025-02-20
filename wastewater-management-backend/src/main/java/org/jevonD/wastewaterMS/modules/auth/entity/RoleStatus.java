package org.jevonD.wastewaterMS.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum RoleStatus {
    DISABLED(0), ENABLED(1);

    @EnumValue  // 注明如何存储到数据库
    private final int value;

    RoleStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoleStatus fromValue(int value) {
        for (RoleStatus status : RoleStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
