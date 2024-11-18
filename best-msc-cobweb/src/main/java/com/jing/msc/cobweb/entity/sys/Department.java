package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.jing.common.core.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author jing
 * @since 2023-10-16 20:52:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_department")
@Tag(name = "单位组织", description = "单位组织表实体类")
public class Department extends BaseModel {

    private static final long serialVersionUID = 5917832367289484375L;

    @Schema(description = "自增主键")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "上级Id")
    @TableField("`parent_id`")
    private Long parentId;

    @Schema(description = "部门名称")
    @TableField("`name`")
    private String name;

    @Schema(description = "状态，0：启用，1禁用")
    @TableField("`status`")
    private Integer status;

    @Schema(description = "备注")
    @TableField("`remarks`")
    private String remarks;

    @Schema(description = "创建人Id")
    @TableField(value = "`creator_id`", fill = FieldFill.INSERT)
    private Long creatorId;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改人Id")
    @TableField(value = "`reviser_id`", fill = FieldFill.INSERT_UPDATE)
    private Long reviserId;

    @Schema(description = "修改时间")
    @TableField(value = "`revision_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime revisionTime;

    @Schema(description = "数据逻辑标识（0：正常，1：已删除）默认：0")
    @TableLogic
    private Integer logicFlag;

    @Schema(description = "乐观锁")
    @TableField("`version`")
    private Integer version;


    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name=" + name +
                ", status=" + status +
                ", remarks=" + remarks +
                ", creatorId=" + creatorId +
                ", createTime=" + createTime +
                ", reviserId=" + reviserId +
                ", revisionTime=" + revisionTime +
                ", logicFlag=" + logicFlag +
                "}";
    }
}
