package com.jing.msc.cobweb.entity.book;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.entity
 * @date : 2021/11/12 10:07
 * @description :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("novel_info")
@Tag(name = "小说信息表", description = "小说信息表")
public class Novel implements Serializable {

    private static final long serialVersionUID = 8964955463358958822L;
    /**
     * 主键Id
     */
    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "小说名称")
    @TableField("name")
    private String name;

    @Schema(description = "小说地址")
    @TableField("`path`")
    private String path;

    @Schema(description = "创建人Id")
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @TableField(value = "revision_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime revisionTime;

    @Schema(description = "数据逻辑标识（0：正常，1：已删除）默认：0")
    @TableLogic
    private Integer logicFlag;

    public Novel(String name, String path) {
        this.name = name;
        this.path = path;
    }

}
