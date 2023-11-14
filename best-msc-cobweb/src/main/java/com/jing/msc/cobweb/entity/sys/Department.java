package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jing.common.core.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author jing
 * @since 2023-10-16 20:52:26
 */
@TableName("sys_department")
@ApiModel(value = "Department对象", description = "")
public class Department extends BaseModel<Department> {

    private static final long serialVersionUID = 5917832367289484375L;

    @ApiModelProperty("上级Id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty("部门名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("状态，0：启用，1禁用")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("备注")
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
