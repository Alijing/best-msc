package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author jing
 * @since 2024-08-21 10:48:22
 */
@Data
@TableName("sys_dict_item")
@Schema(description = "")
public class DictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "字典Id")
    @TableField("dict_id")
    private Long dictId;

    @Schema(description = "字典项名称")
    @TableField("name")
    private String name;

    @Schema(description = "字典项描述")
    @TableField("desc")
    private String desc;

    @Schema(description = "排序")
    @TableField("sort")
    private BigDecimal sort;

    @Schema(description = "是否允许修改，0：不能修改，1：允许修改")
    @TableField("allow_mdf")
    private Integer allowMdf;

    @Schema(description = "是否允许删除，0：不能删除，1：允许删除")
    @TableField("allow_del")
    private Integer allowDel;

    @Schema(description = "逻辑删除标记（0：默认未删除，1：已删除）")
    @TableField("logic_del")
    @TableLogic
    private Integer logicDel;


}
