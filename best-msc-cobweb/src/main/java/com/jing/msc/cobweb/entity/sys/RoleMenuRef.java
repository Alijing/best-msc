package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;

/**
 * <p>
 * 系统角色与权限的关联表
 * </p>
 *
 * @author jing
 * @since 2023-07-21 17:33:12
 */
@TableName("sys_role_menu_ref")
@Tag(name = "系统角色", description = "系统角色与权限的关联表")
public class RoleMenuRef implements Serializable {

    private static final long serialVersionUID = 1L;

   @Schema(name = "角色Id")
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private Long roleId;

   @Schema(name = "权限Id")
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private Long menuId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "RoleMenuRef{" +
                "roleId=" + roleId +
                ", menuId=" + menuId +
                "}";
    }
}
