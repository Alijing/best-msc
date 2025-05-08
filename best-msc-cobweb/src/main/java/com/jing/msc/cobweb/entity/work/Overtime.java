package com.jing.msc.cobweb.entity.work;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 加班信息
 *
 * @author : jing
 * @since : 2025/2/18 14:26
 */
@Data
@TableName("work_overtime") // 指定数据库表名
@Schema(name = "工作相关", description = "加班信息")
public class Overtime {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "自增主键")
    private Long id;

    @TableField("start_time")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @TableField("end_time")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @TableField("duration")
    @Schema(description = "加班时长，单位：小时")
    private Double duration;

    @TableField("project_code")
    @Schema(description = "项目编码")
    private String projectCode;

    @TableField("project")
    @Schema(description = "项目名称")
    private String project;

    @TableField("work")
    @Schema(description = "工作内容")
    private String work;

}
