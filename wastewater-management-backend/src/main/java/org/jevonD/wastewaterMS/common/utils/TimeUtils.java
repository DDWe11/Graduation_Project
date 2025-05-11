package org.jevonD.wastewaterMS.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {

    private static final ZoneId BEIJING_ZONE = ZoneId.of("Asia/Shanghai");

    /**
     * 获取北京时间的 LocalDateTime
     */
    public static LocalDateTime nowBeijing() {
        return ZonedDateTime.now(BEIJING_ZONE).toLocalDateTime();
    }

    /**
     * 将当前 LocalDateTime 转换为北京时间
     */
    public static LocalDateTime toBeijing(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(BEIJING_ZONE).toLocalDateTime();
    }
}
