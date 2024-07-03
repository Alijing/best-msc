package com.jing.common.core.base;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.base
 * @date : 2021/4/26 9:41
 * @description :
 */
public class BaseModel implements Serializable {
    private static final long serialVersionUID = -5569718322628749683L;
    /**
     * 主键
     */
    @ApiModelProperty("自增主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected Long id;
    /**
     * 创建人Id
     */
    @ApiModelProperty("创建人Id")
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    protected Long creatorId;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;
    /**
     * 修改者Id
     */
    @ApiModelProperty("修改者Id")
    @TableField(value = "reviser_id", fill = FieldFill.INSERT_UPDATE)
    protected Long reviserId;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @TableField(value = "revision_time", fill = FieldFill.INSERT)
    protected LocalDateTime revisionTime;
    /**
     * 逻辑删除字段
     */
    @TableLogic
    @ApiModelProperty("数据逻辑标识（0：正常，1：已删除）默认：0")
    @TableField(value = "logic_flag", fill = FieldFill.INSERT)
    protected Integer logicFlag;
    /**
     * 乐观锁
     */
    @Version
    @ApiModelProperty("乐观锁")
    protected Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getReviserId() {
        return reviserId;
    }

    public void setReviserId(Long reviserId) {
        this.reviserId = reviserId;
    }

    public LocalDateTime getRevisionTime() {
        return revisionTime;
    }

    public void setRevisionTime(LocalDateTime revisionTime) {
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
}
