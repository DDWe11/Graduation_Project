package org.jevonD.wastewaterMS.modules.process.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("process_unit")
public class ProcessUnit {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name; // 工艺单元名称，如 AAO 池

    private String type; // 处理类型，如物理、生化

    private Integer sequence; // 处理顺序编号

    private String description; // 工艺说明

    private LocalDateTime createTime;
}
