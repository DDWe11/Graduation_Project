package org.jevonD.wastewaterMS.common.utils;

import java.util.Map;

/**
 * 控制台多设备倒计时进度条工具
 * 用于展示每个设备距离下次推送采集的剩余时间
 */
public class DeviceCountdownBar {

    /**
     * 打印所有设备的倒计时进度条
     * @param deviceRemainMap   设备ID -> 剩余秒数
     * @param deviceFreqMap     设备ID -> 周期秒数（频率）
     * @param deviceNameMap     设备ID -> 设备名（可选增强可读性）
     */
    public static void print(Map<Long, Integer> deviceRemainMap,
                             Map<Long, Integer> deviceFreqMap,
                             Map<Long, String> deviceNameMap) {
        StringBuilder sb = new StringBuilder("\033[H\033[2J"); // 清屏
        for (Map.Entry<Long, Integer> entry : deviceRemainMap.entrySet()) {
            long id = entry.getKey();
            int remain = entry.getValue();
            int total = deviceFreqMap.getOrDefault(id, 1);
            String name = deviceNameMap.getOrDefault(id, String.valueOf(id));
            int percent = (int) ((total - remain) * 100.0 / total);

            // %-10s 设备名（10字符左对齐），[%d] 设备ID, %3d%% 百分比, %s 进度条, %4ds 剩余秒
            sb.append(String.format("%-12s[%d] %3d%% %s %4ds\n",
                    name, id, percent, generateBar(percent, 18), remain));
        }
        System.out.print(sb.toString());
    }

    private static String generateBar(int percent, int width) {
        int complete = percent * width / 100;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            sb.append(i < complete ? '█' : '░');
        }
        sb.append("]");
        return sb.toString();
    }
}