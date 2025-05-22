package org.jevonD.wastewaterMS.modules.data.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.jevonD.wastewaterMS.modules.data.dto.history.request.PlantDrugUsageQueryReq;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDrugUsageQueryResp;
import org.jevonD.wastewaterMS.modules.data.dto.history.response.PlantDrugUsageQueryResp.PlantDrugUsageItem;
import org.jevonD.wastewaterMS.modules.data.entity.PlantDrugUsage;
import org.jevonD.wastewaterMS.modules.data.repository.PlantDrugUsageRepository;
import org.jevonD.wastewaterMS.modules.data.service.PlantDrugUsageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantDrugUsageServiceImpl implements PlantDrugUsageService {

    private final PlantDrugUsageRepository plantDrugUsageRepository;

    @Override
    public PlantDrugUsageQueryResp queryDrugUsage(PlantDrugUsageQueryReq req) {
        Page<PlantDrugUsage> page = new Page<>(req.getPage(), req.getSize());
        List<PlantDrugUsage> dataList = plantDrugUsageRepository.findPageByConditions(
                page, req.getStartDate(), req.getEndDate()
        );

        List<PlantDrugUsageItem> records = new ArrayList<>();
        int baseIdx = (req.getPage() - 1) * req.getSize() + 1;
        for (int i = 0; i < dataList.size(); i++) {
            PlantDrugUsageItem item = new PlantDrugUsageItem();
            BeanUtils.copyProperties(dataList.get(i), item);
            item.setId((long) (baseIdx + i)); // 用序号作为id
            records.add(item);
        }

        PlantDrugUsageQueryResp resp = new PlantDrugUsageQueryResp();
        resp.setTotal(page.getTotal());
        resp.setRecords(records);
        return resp;
    }
}