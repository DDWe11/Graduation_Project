import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import org.jevonD.wastewaterMS.common.utils.SensorSimulateUtils;

public class SensorSimulateUtilsTest {

    @Test
    public void testCurrentPeriodFeatureLookup() {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int h = (now.getHour() / 3) * 3;
        String periodStart = String.format("%02d:00", h);

        String sensorType = "flow_in"; // 你可以改成其他存在的类型测试
        SensorSimulateUtils.RefFeature f = SensorSimulateUtils.getFeature(sensorType, month, day, periodStart);

        System.out.println("查找key: " + sensorType + "|" + month + "|" + day + "|" + periodStart);
        assertNotNull(f, "未查到参考特征，请检查参考数据是否覆盖该时间段");

        System.out.printf("特征参数: mean=%.2f, std=%.2f, min=%.2f, max=%.2f, median=%.2f, q1=%.2f, q3=%.2f\n",
                f.mean, f.std, f.min, f.max, f.median, f.q1, f.q3);
    }

    @Test
    public void testEachGenerateMethod() {
        LocalDateTime now = LocalDateTime.now();
        boolean success = true;

        try {
            double flowIn = SensorSimulateUtils.generateFlowIn(now);
            System.out.printf("[flow_in] 仿真值：%.2f\n", flowIn);

            double flowOut = SensorSimulateUtils.generateFlowOut(now);
            System.out.printf("[flow_out] 仿真值：%.2f\n", flowOut);

            double flowUnbound = SensorSimulateUtils.generateFlow(now);
            System.out.printf("[flow_unbound] 仿真值：%.2f\n", flowUnbound);

            double turbIn = SensorSimulateUtils.generateTurbidityIn(now);
            System.out.printf("[turb_in] 仿真值：%.2f\n", turbIn);

            double turbOut = SensorSimulateUtils.generateTurbidityOut(now);
            System.out.printf("[turb_out] 仿真值：%.2f\n", turbOut);

            double turb = SensorSimulateUtils.generateTurbidity(now);
            System.out.printf("[turb_unbound] 仿真值：%.2f\n", turb);

            double temp = SensorSimulateUtils.generateTemperature(now);
            System.out.printf("[temperature] 仿真值：%.2f\n", temp);

            double humid = SensorSimulateUtils.generateHumidity(now);
            System.out.printf("[humidity] 仿真值：%.2f\n", humid);

            double power = SensorSimulateUtils.generatePower(now);
            System.out.printf("[power] 仿真值：%.2f\n", power);

        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        assertTrue(success, "至少一个仿真方法发生异常");
    }
}