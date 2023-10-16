package com.jing.msc.security.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统权限信息表
 * </p>
 *
 * @author jing
 * @since 2023-07-21 17:33:12
 */
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "系统权限信息表")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("权限key")
    @TableField("perm_key")
    private String permKey;

    @ApiModelProperty("状态，0：可用，1：禁用")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("创建人Id")
    @TableField("creator_id")
    private Long creatorId;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("修改人Id")
    @TableField("reviser_id")
    private Long reviserId;

    @ApiModelProperty("修改时间")
    @TableField("revision_time")
    private Date revisionTime;

    @ApiModelProperty("数据逻辑标识（0：正常，1：已删除）默认：0")
    @TableField("logic_flag")
    private Integer logicFlag;

    @ApiModelProperty("乐观锁")
    @TableField("version")
    private Integer version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermKey() {
        return permKey;
    }

    public void setPermKey(String permKey) {
        this.permKey = permKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getLogicFlag() {
        return logicFlag;
    }

    public void setLogicFlag(Integer logicFlag) {
        this.logicFlag = logicFlag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name=" + name +
                ", permKey=" + permKey +
                ", status=" + status +
                ", creatorId=" + creatorId +
                ", createTime=" + createTime +
                ", reviserId=" + reviserId +
                ", revisionTime=" + revisionTime +
                ", logicFlag=" + logicFlag +
                ", version=" + version +
                "}";
    }
}
