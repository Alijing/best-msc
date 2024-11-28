package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jing.msc.cobweb.enums.sys.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : jing
 * @since : 2024/11/21 17:29
 */

@Data
@TableName("sys_role_menu_permission")
@Tag(name = "角色与菜单权限", description = "角色与菜单权限表")
public class RoleMenuPermission implements Serializable {

    private static final long serialVersionUID = 745758764023381877L;

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色id")
    @TableField("`role_id`")
    private Long roleId;

    @Schema(description = "菜单id")
    @TableField("`menu_id`")
    private Long menuId;

    @Schema(description = "菜单权限")
    @TableField("`permission`")
    private Permission permission;

}
