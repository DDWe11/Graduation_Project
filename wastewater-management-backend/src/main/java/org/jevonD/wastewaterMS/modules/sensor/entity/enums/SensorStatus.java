package org.jevonD.wastewaterMS.modules.sensor.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SensorStatus {
    DISABLED(0),
    ENABLED(1);

    @EnumValue
    private final int value;

    SensorStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SensorStatus fromValue(int value) {
        for (SensorStatus status : SensorStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown SensorStatus value: " + value);
    }
}
