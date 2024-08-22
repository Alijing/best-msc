package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jing.common.core.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *
 * </p>
 *
 * @author jing
 * @since 2023-10-16 20:52:26
 */
@TableName("sys_department")
@Tag(name = "单位组织", description = "单位组织表实体类")
public class Department extends BaseModel {

    private static final long serialVersionUID = 5917832367289484375L;

    @Schema(name = "上级Id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(name = "部门名称")
    @TableField("name")
    private String name;

    @Schema(name = "状态，0：启用，1禁用")
    @TableField("status")
    private Integer status;

    @Schema(name = "备注")
    @TableField("remarks")
    private String remarks;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

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
