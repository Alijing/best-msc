package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.jing.msc.cobweb.enums.sys.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色信息表
 * </p>
 *
 * @author jing
 * @since 2024-07-04 16:21:35
 */
@Data
@TableName("sys_role")
@Schema(description = "系统角色信息表")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色名称")
    @TableField("name")
    private String name;

    @Schema(description = "角色编码")
    @TableField("code")
    private String code;

    @Schema(description = "状态：0：可用，1：禁用")
    @TableField("status")
    private RoleStatusEnum status;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

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


    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
