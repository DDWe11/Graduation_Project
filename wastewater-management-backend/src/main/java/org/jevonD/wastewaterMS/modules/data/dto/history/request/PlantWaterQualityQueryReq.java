package org.jevonD.wastewaterMS.modules.data.dto.history.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PlantWaterQualityQueryReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer page;

    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页最小为1")
    @Max(value = 50, message = "每页最大50条")
    private Integer size;
}