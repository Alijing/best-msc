package com.jing.msc.cobweb.entity.work;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/7/18 16:01
 */
@Data
@Tag(name = "工作相关", description = "禅道项目")
public class Project implements Serializable {

    private static final long serialVersionUID = -2830023723143854196L;

    @Schema(description = "项目Id")
    private Long id;

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "截止日期")
    private LocalDate endDate;

    @Schema(description = "任务列表")
    private List<TaskInfo> tasks;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks size =" + tasks.size() +
                '}';
    }

}
