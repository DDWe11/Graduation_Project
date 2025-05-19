package org.jevonD.wastewaterMS.common.utils;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.*;
import jakarta.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

public class ProgressBarManager {

    private final Map<Long, Integer> deviceRemainMap = new ConcurrentHashMap<>();
    private final Map<Long, Integer> deviceFreqMap = new ConcurrentHashMap<>();
    private final Map<Long, String> deviceNameMap = new ConcurrentHashMap<>();
    private static final Map<String, Object> sessionSendLock = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final AtomicBoolean running = new AtomicBoolean(false);

    public static void sendSync(Session session, String text) throws Exception {
        Object lock = sessionSendLock.computeIfAbsent(session.getId(), k -> new Object());
        synchronized (lock) {
            session.getBasicRemote().sendText(text);
        }
    }


    public void start() {
        if (running.compareAndSet(false, true)) {
            scheduler.scheduleAtFixedRate(this::tickAndPrint, 0, 1, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        running.set(false);
        scheduler.shutdownNow();
        System.out.println("采集全部结束，进度条停止。");
    }

    public void addDevice(long id, int freq, String name) {
        deviceRemainMap.put(id, freq);
        deviceFreqMap.put(id, freq);
        deviceNameMap.put(id, name);
    }

    public void removeDevice(long id) {
        deviceRemainMap.remove(id);
        deviceFreqMap.remove(id);
        deviceNameMap.remove(id);
    }

    /**
     * 设备采集一次后重置倒计时
     */
    public void resetRemain(long id) {
        Integer freq = deviceFreqMap.get(id);
        if (freq != null) {
            deviceRemainMap.put(id, freq);
        }
    }

    private void tickAndPrint() {
        for (Map.Entry<Long, Integer> entry : deviceRemainMap.entrySet()) {
            int remain = entry.getValue();
            if (remain > 0) deviceRemainMap.put(entry.getKey(), remain - 1);
        }
        print();
    }

    private void print() {
        StringBuilder sb = new StringBuilder("\033[H\033[2J"); // 清屏
        for (Map.Entry<Long, Integer> entry : deviceRemainMap.entrySet()) {
            long id = entry.getKey();
            int remain = entry.getValue();
            int total = deviceFreqMap.getOrDefault(id, 1);
            String name = deviceNameMap.getOrDefault(id, String.valueOf(id));
            int percent = (int) ((total - remain) * 100.0 / total);
            sb.append(String.format("%-12s [%d] %3d%% %s %4ds\n",
                    name, id, percent, generateBar(percent, 18), remain));
        }
        System.out.print(sb.toString());
    }

    private String generateBar(int percent, int width) {
        int complete = percent * width / 100;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            sb.append(i < complete ? '█' : '░');
        }
        sb.append("]");
        return sb.toString();
    }
}