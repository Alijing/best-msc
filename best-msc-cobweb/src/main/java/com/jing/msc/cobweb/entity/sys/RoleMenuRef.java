package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统角色与权限的关联表
 * </p>
 *
 * @author jing
 * @since 2023-07-21 17:33:12
 */
@Data
@TableName("sys_role_menu_ref")
@Tag(name = "系统角色", description = "系统角色与权限的关联表")
public class RoleMenuRef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "角色Id")
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private Long roleId;

    @Schema(name = "权限Id")
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private Long menuId;

    @Override
    public String toString() {
        return "RoleMenuRef{" +
                "roleId=" + roleId +
                ", menuId=" + menuId +
                "}";
    }

}
