package org.jevonD.wastewaterMS.modules.sensor.entity.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;

public enum DeviceState {
    OFFLINE(0, "离线"),
    ONLINE(1, "在线"),
    ABNORMAL(2, "异常");

    @EnumValue
    private final int value;
    private final String description;

    DeviceState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static DeviceState fromValue(int value) {
        for (DeviceState state : DeviceState.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown DeviceState value: " + value);
    }
}