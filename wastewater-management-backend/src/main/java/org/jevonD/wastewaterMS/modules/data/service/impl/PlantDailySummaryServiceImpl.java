package org.jevonD.wastewaterMS.modules.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantDailyDataQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDailyDataQueryResp;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDailyDataQueryResp.PlantDailyDataItem;
import org.jevonD.wastewaterMS.modules.data.entity.PlantDailySummary;
import org.jevonD.wastewaterMS.modules.data.entity.SensorData;
import org.jevonD.wastewaterMS.modules.data.repository.PlantDailySummaryRepository;
import org.jevonD.wastewaterMS.modules.data.service.PlantDailySummaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantDailySummaryServiceImpl implements PlantDailySummaryService {

    private final PlantDailySummaryRepository plantDailySummaryRepository;

    @Override
    public PlantDailyDataQueryResp queryDailyData(PlantDailyDataQueryReq req) {

        Page<PlantDailySummary> page = new Page<>(req.getPage(), req.getSize());

        // 这里 repository 查询返回当前页实际数据
        List<PlantDailySummary> dataList = plantDailySummaryRepository.findPageByConditions(
                page,
                req.getStartDate(),
                req.getEndDate(),
                req.getMinEmission(),
                req.getMaxEmission()
        );

        // 组装分页序号和 DTO
        List<PlantDailyDataItem> records = new ArrayList<>();
        int baseIdx = (req.getPage() - 1) * req.getSize() + 1;

        for (int i = 0; i < dataList.size(); i++) {
            PlantDailySummary entity = dataList.get(i);
            PlantDailyDataItem item = new PlantDailyDataItem();
            // 用序号作为id
            item.setId((long) (baseIdx + i));

            // 复制其余字段
            item.setRecordDate(entity.getRecordDate());
            item.setWaterVolume(entity.getWaterVolume());
            item.setElectricity(entity.getElectricity());
            item.setTotalEmission(entity.getTotalEmission());
            item.setEmissionIntensity(entity.getEmissionIntensity());
            item.setTotalChemicals(entity.getTotalChemicals());
            item.setSludgeMass(entity.getSludgeMass());
            item.setCodRemovalRate(entity.getCodRemovalRate());
            item.setTnRemovalRate(entity.getTnRemovalRate());
            item.setNh3nRemovalRate(entity.getNh3nRemovalRate());
            item.setSsRemovalRate(entity.getSsRemovalRate());
            item.setTpRemovalRate(entity.getTpRemovalRate());

            records.add(item);
        }

        PlantDailyDataQueryResp resp = new PlantDailyDataQueryResp();
        resp.setTotal(page.getTotal());   // MyBatis-Plus Page 对象的 total
        resp.setRecords(records);
        return resp;
    }
}