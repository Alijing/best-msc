package com.jing.msc.cobweb.entity.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : jing
 * @since : 2024/11/27 11:30
 */
@Data
@NoArgsConstructor
@Tag(name = "系统菜单", description = "菜单简要信息")
public class MenuSimpleInfo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "上级Id")
    private Long parentId;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单权限")
    private String permission;

    @Schema(description = "子菜单")
    private List<MenuSimpleInfo> children;

    public MenuSimpleInfo(Long parentId, Long id, String name) {
        this.parentId = parentId;
        this.id = id;
        this.name = name;
    }

}
