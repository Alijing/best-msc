package com.jing.msc.cobweb.entity.work;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author : jing
 * @since : 2024/7/16 14:10
 */
@Data
@Tag(name = "工作相关", description = "禅道个人任务日志")
public class TaskLog implements Serializable {

    private static final long serialVersionUID = 60825214037290969L;

    @Schema(description = "工作日志Id")
    private Long id;

    @Schema(description = "任务Id")
    private Long taskId;

    @Schema(description = "日志内容")
    private String title;

    @Schema(description = "工作日期")
    private LocalDate workDate;

    @Schema(description = "日志详情 url")
    private String url;

    @Schema(description = "用时（小时）")
    private Double consumed;

    @Override
    public String toString() {
        return "TaskLog{" +
                "id=" + id +
                ", workDate=" + workDate +
                ", consumed=" + consumed +
                '}';
    }

}
