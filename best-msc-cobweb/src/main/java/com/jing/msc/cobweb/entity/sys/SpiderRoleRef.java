package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 系统用户与角色关联表
 * </p>
 *
 * @author jing
 * @since 2023-07-21 17:33:12
 */
@TableName("sys_spider_role_ref")
@ApiModel(value = "SpiderRoleRef对象", description = "系统用户与角色关联表")
public class SpiderRoleRef implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户Id")
    @TableId(value = "spider_id", type = IdType.ASSIGN_ID)
    private Long spiderId;

    @ApiModelProperty("角色Id")
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private Long roleId;


    public Long getSpiderId() {
        return spiderId;
    }

    public void setSpiderId(Long spiderId) {
        this.spiderId = spiderId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "SpiderRoleRef{" +
                "spiderId=" + spiderId +
                ", roleId=" + roleId +
                "}";
    }
}
