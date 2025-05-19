package org.jevonD.wastewaterMS.modules.data.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 各类传感器类型的默认采集频率和最小合法频率配置
 * 可在后端业务层、WebSocket定时任务等多处复用
 */
public class CollectFrequencyConfig {

    // 类型英文CODE -> 默认采集频率（单位：秒）
    public static final Map<String, Integer> SENSOR_TYPE_DEFAULT_FREQ = new HashMap<>() {{
        put("TEMP", 600);        // 温度每10秒
        put("HUMIDITY", 600);    // 湿度每10秒
        put("FLOW", 60);         // 流量每1秒
        put("TURBIDITY", 60);    // 浊度每5秒
        put("POWER", 600);        // 功率每5秒
    }};

    // 类型英文CODE -> 最小合法采集频率（单位：秒）
    public static final Map<String, Integer> SENSOR_TYPE_MIN_FREQ = new HashMap<>() {{
        put("TEMP", 60);         // 温度最小5秒
        put("HUMIDITY", 60);     // 湿度最小5秒
        put("FLOW", 5);         // 流量最小1秒
        put("TURBIDITY", 5);    // 浊度最小3秒
        put("POWER", 60);        // 功率最小3秒
    }};

    /**
     * 获取指定类型的默认频率，若无匹配返回10（全局兜底）
     */
    public static int getDefaultFreq(String typeCode) {
        return SENSOR_TYPE_DEFAULT_FREQ.getOrDefault(typeCode, 10);
    }

    /**
     * 获取指定类型的最小合法频率，若无匹配返回1
     */
    public static int getMinFreq(String typeCode) {
        return SENSOR_TYPE_MIN_FREQ.getOrDefault(typeCode, 1);
    }
}