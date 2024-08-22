package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author jing
 * @since 2024-08-21 10:48:22
 */
@Data
@TableName("sys_dict")
@Schema(description = "")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "字典编码")
    @TableField("code")
    private String code;

    @Schema(description = "字典类型，0：系统类，1：业务类")
    @TableField("type")
    private Integer type;

    @Schema(description = "字典描述")
    @TableField("desc")
    private String desc;

    @Schema(description = "创建人Id")
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改人Id")
    @TableField(value = "reviser_id", fill = FieldFill.INSERT_UPDATE)
    private Long reviserId;

    @Schema(description = "修改时间")
    @TableField(value = "revision_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime revisionTime;

    @Schema(description = "数据逻辑标识（0：正常，1：已删除）默认：0")
    @TableField("logic_flag")
    private Integer logicFlag;

    @Schema(description = "乐观锁")
    @TableField("version")
    private Integer version;


}
