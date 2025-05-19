package org.jevonD.wastewaterMS.modules.data.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jevonD.wastewaterMS.modules.data.entity.SensorData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SensorDataRepositoryTest {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Test
    public void testFindPageByConditions() {
        Page<SensorData> page = new Page<>(1, 10);
        List<Long> sensorIds = Arrays.asList(1923057976553472002L, 1923057963941199873L);
        Integer alarmFlag = 1;
        LocalDateTime start = LocalDateTime.of(2024, 5, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now();

        List<SensorData> list = sensorDataRepository.findPageByConditions(page, sensorIds, alarmFlag, start, end);

        System.out.println("总数: " + page.getTotal());
        list.forEach(System.out::println);
    }
}