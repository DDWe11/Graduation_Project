package org.jevonD.wastewaterMS.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SensorSimulateUtils {

    public static class RefFeature {
        public double mean, std, min, max, median, q1, q3;
        public RefFeature(double mean, double std, double min, double max, double median, double q1, double q3) {
            this.mean = mean;
            this.std = std;
            this.min = min;
            this.max = max;
            this.median = median;
            this.q1 = q1;
            this.q3 = q3;
        }
    }

    static Map<String, RefFeature> refMap = new HashMap<>();
    static Random random = new Random();

    static {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(SensorSimulateUtils.class.getResourceAsStream("/sensor_feature_reference.csv"), StandardCharsets.UTF_8))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                String key = arr[0] + "|" + arr[1] + "|" + arr[2] + "|" + arr[3];
                refMap.put(key, new RefFeature(
                        Double.parseDouble(arr[4]), Double.parseDouble(arr[5]),
                        Double.parseDouble(arr[6]), Double.parseDouble(arr[7]),
                        Double.parseDouble(arr[8]), Double.parseDouble(arr[9]), Double.parseDouble(arr[10])
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("csv读取失败", e);
        }
    }

    public static double generateFlowIn(LocalDateTime time) {
        return generateValue("flow_in", time);
    }

    public static double generateFlowOut(LocalDateTime time) {
        return generateValue("flow_out", time);
    }

    public static double generateFlow(LocalDateTime time) {
        double base = generateValue("flow_in", time);
        return round(base * 0.95); // 未绑定流量略低
    }

    public static double generateTurbidityIn(LocalDateTime time) {
        return generateValue("turb_in", time);
    }


    public static double generateTurbidityOut(LocalDateTime time) {
        return generateValue("turb_out", time);
    }

    public static double generateTurbidity(LocalDateTime time) {
        double base = generateValue("turb_in", time);
        return round(base * 0.85);
    }

    public static double generateTemperature(LocalDateTime time) {
        return generateValue("temperature", time);
    }

    public static double generateHumidity(LocalDateTime time) {
        return generateValue("humidity", time);
    }

    public static double generatePower(LocalDateTime time) {
        return generateValue("power", time);
    }

    private static double generateValue(String type, LocalDateTime time) {
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();
        int hourBlock = (time.getHour() / 3) * 3;
        String key = type + "|" + month + "|" + day + "|" + String.format("%02d:00", hourBlock);
        RefFeature f = refMap.get(key);
        if (f == null) throw new IllegalArgumentException("特征不存在: " + key);

        double val = f.mean + random.nextGaussian() * f.std;
        if (val < f.min) val = f.min;
        if (val > f.max) val = f.max;
        return round(val);
    }

    private static double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    public static RefFeature getFeature(String sensorType, int month, int day, String periodStart) {
        return refMap.get(sensorType + "|" + month + "|" + day + "|" + periodStart);
    }
}
