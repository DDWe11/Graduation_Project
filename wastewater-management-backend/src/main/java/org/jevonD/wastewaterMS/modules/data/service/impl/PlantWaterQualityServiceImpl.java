package org.jevonD.wastewaterMS.modules.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantWaterQualityQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantWaterQualityQueryResp;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantWaterQualityQueryResp.PlantWaterQualityItem;
import org.jevonD.wastewaterMS.modules.data.entity.PlantWaterQuality;
import org.jevonD.wastewaterMS.modules.data.repository.PlantWaterQualityRepository;
import org.jevonD.wastewaterMS.modules.data.service.PlantWaterQualityService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantWaterQualityServiceImpl implements PlantWaterQualityService {

    private final PlantWaterQualityRepository plantWaterQualityRepository;

    @Override
    public PlantWaterQualityQueryResp queryWaterQuality(PlantWaterQualityQueryReq req) {
        Page<PlantWaterQuality> page = new Page<>(req.getPage(), req.getSize());
        List<PlantWaterQuality> dataList = plantWaterQualityRepository.findPageByConditions(
                page, req.getStartDate(), req.getEndDate()
        );
        List<PlantWaterQualityItem> records = new ArrayList<>();
        int baseIdx = (req.getPage() - 1) * req.getSize() + 1;
        for (int i = 0; i < dataList.size(); i++) {
            PlantWaterQualityItem item = new PlantWaterQualityItem();
            BeanUtils.copyProperties(dataList.get(i), item);
            item.setId((long) (baseIdx + i)); // 用序号
            records.add(item);
        }

        PlantWaterQualityQueryResp resp = new PlantWaterQualityQueryResp();
        resp.setTotal(page.getTotal());
        resp.setRecords(records);
        return resp;
    }
}