package com.jing.msc.cobweb.entity.video;

import com.baomidou.mybatisplus.annotation.*;
import com.jing.msc.cobweb.enums.video.VideoStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author jing
 * @since 2024-10-25 10:01:03
 */
@Data
@TableName("video_taste")
@Schema(description = "视频 - 情趣视频表")
public class VideoTaste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增主键")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "车牌号")
    @TableField("`number`")
    private String number;

    @Schema(description = "名称")
    @TableField("`name`")
    private String name;

    @Schema(description = "演员")
    @TableField("`performer`")
    private String performer;

    @Schema(description = "发行时间")
    @TableField("`release_date`")
    private LocalDate releaseDate;

    @Schema(description = "评分")
    @TableField("`rating`")
    private Integer rating;

    @Schema(description = "BT链接或下载口令")
    @TableField("`bt_link`")
    private String btLink;

    @Schema(description = "状态，0：未下载，1：已下载，2：已观看")
    @TableField("`status`")
    private VideoStatusEnum status;

    @Schema(description = "创建者用户Id")
    @TableField(value = "`gmt_creator`", fill = FieldFill.INSERT)
    private Long gmtCreator;

    @Schema(description = "创建时间")
    @TableField(value = "`gmt_create`", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @Schema(description = "更新时间")
    @TableField(value = "`gmt_modified`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
