package com.jing.msc.cobweb.entity.sys.vo;

import com.jing.msc.cobweb.entity.sys.MenuName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/7/5 13:56
 */
@Data
@Tag(name = "系统菜单", description = "前端路由实体")
public class RouteRecord implements Serializable {

    private static final long serialVersionUID = -1858496324294208089L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "上级Id")
    private Long parentId;

    @Schema(description = "路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题")
    private String name;

    @Schema(description = "菜单路径")
    private String path;

    @Schema(description = "noredirect，当设置 noredirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    @Schema(description = "前端组件地址")
    private String component;

    @Schema(description = "路由自定义属性")
    private RouteMetaCustom meta;

    @Schema(description = "菜单名称国际化")
    private List<MenuName> i18ns;

    @Schema(description = "子菜单")
    private List<RouteRecord> children;


}
