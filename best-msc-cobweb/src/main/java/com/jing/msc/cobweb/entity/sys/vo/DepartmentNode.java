package com.jing.msc.cobweb.entity.sys.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Date;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.sys.vo
 * @date : 2023/11/10 14:43
 * @description :
 */
@Tag(name = "单位组织", description = "单位组织节点实体类")
public class DepartmentNode {

    @Schema(name = "自增主键")
    private Long id;

    @Schema(name = "上级Id")
    private Long parentId;

    @Schema(name = "部门名称")
    private String name;

    @Schema(name = "状态，0：启用，1禁用")
    private Integer status;

    @Schema(name = "备注")
    private String remarks;

    @Schema(name = "创建人Id")
    private Long creatorId;

    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "修改人Id")
    private Long reviserId;

    @Schema(name = "修改时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date revisionTime;

    private List<DepartmentNode> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getReviserId() {
        return reviserId;
    }

    public void setReviserId(Long reviserId) {
        this.reviserId = reviserId;
    }

    public Date getRevisionTime() {
        return revisionTime;
    }

    public void setRevisionTime(Date revisionTime) {
        this.revisionTime = revisionTime;
    }

    public List<DepartmentNode> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentNode> children) {
        this.children = children;
    }
}
