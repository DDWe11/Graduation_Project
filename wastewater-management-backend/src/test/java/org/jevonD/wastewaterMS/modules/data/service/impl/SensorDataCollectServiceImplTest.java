package org.jevonD.wastewaterMS.modules.data.service.impl;

import org.jevonD.wastewaterMS.modules.data.dto.sensor.request.SensorDataCollectReq;
import org.jevonD.wastewaterMS.modules.data.dto.sensor.response.SensorDataCollectResp;
import org.jevonD.wastewaterMS.modules.data.service.SensorDataCollectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest(properties = {"spring.profiles.active=test"})
public class SensorDataCollectServiceImplTest {

    @Autowired
    private SensorDataCollectService sensorDataCollectService;

    @Test
    public void testCollectAndStoreSensorData() {
        System.out.println("当前 JVM 默认时区: " + java.util.TimeZone.getDefault().getID());
        SensorDataCollectReq req = new SensorDataCollectReq();
        List<SensorDataCollectResp> result = sensorDataCollectService.collectAndStoreSensorData(req);
        result.forEach(System.out::println);
    }
}
