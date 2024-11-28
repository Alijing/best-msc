package com.jing.msc.cobweb.entity.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : jing
 * @since : 2024/11/21 19:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "角色与菜单的权限信息")
public class RoleMenuPerm implements Serializable {

    private static final long serialVersionUID = -5775275551816468192L;

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "菜单id")
    private Long menuId;

    @Schema(description = "权限信息")
    private String permission;

}
