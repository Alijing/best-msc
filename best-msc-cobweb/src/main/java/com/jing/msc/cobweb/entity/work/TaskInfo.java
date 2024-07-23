package com.jing.msc.cobweb.entity.work;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author : jing
 * @since : 2024/7/16 14:10
 */
@Data
@Schema(description = "禅道个人任务")
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = 60825214037290969L;

    @Schema(description = "任务Id")
    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "截止日期")
    private LocalDate deadline;

    @Schema(description = "任务描述")
    private String description;

    @Override
    public String toString() {
        return "TaskInfo{" +
                ", id=" + id +
                ", name=" + name +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", deadline=" + deadline +
                '}';
    }

}
