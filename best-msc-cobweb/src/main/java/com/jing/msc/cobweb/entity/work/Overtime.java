package com.jing.msc.cobweb.entity.work;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.List;

/**
 * 加班信息
 *
 * @author : jing
 * @since : 2025/2/18 14:26
 */
@Data
@Tag(name = "工作相关", description = "加班信息")
public class Overtime {

    @Schema(description = "工作日期")
    private String wordDate;

    @Schema(description = "加班时间段")
    private String overtime;

    @Schema(description = "加班时长")
    private Double duration;

    @Schema(description = "加班项目")
    private List<String> execution;

    @Schema(description = "加班内容")
    private List<String> title;

}
