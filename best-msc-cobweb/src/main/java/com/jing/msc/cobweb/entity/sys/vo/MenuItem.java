package com.jing.msc.cobweb.entity.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/7/4 17:02
 */
@Data
@Tag(name = "系统菜单", description = "菜单信息实体")
public class MenuItem implements Serializable {

    private static final long serialVersionUID = -1858496324294208089L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "上级菜单Id，当该节点为根节点时该字段值为0")
    private Long parentId;

    @Schema(description = "路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题")
    private String name;

    @Schema(description = "菜单路径")
    private String path;

    @Schema(description = "noredirect，当设置 noredirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    @Schema(description = "前端组件地址")
    private String component;

    @Schema(description = "路由在侧边栏和面包屑中展示的名字")
    private String title;

    @Schema(description = "当设置 true 的时候该路由不会再侧边栏出现 如404，login等页面(默认 false)")
    private Boolean hidden;

    @Schema(description = "当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式，只有一个时，会将那个子路由当做根路由显示在侧边栏，若你想不管路由下面的 children 声明的个数都显示你的根路由， 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，	一直显示根路由(默认 false)")
    private Boolean alwaysShow;

    @Schema(description = "路由的图标")
    private String icon;

    @Schema(description = "如果设置为true，则不会被 <keep-alive> 缓存(默认 false)")
    private Boolean noCache;

    @Schema(description = "如果设置为false，则不会在breadcrumb面包屑中显示(默认 true)")
    private Boolean breadcrumb;

    @Schema(description = "如果设置为true，则会一直固定在tag项中(默认 false)")
    private Boolean affix;

    @Schema(description = "如果设置为true，则不会出现在tag中(默认 false)")
    private Boolean noTagsView;

    @Schema(description = "显示高亮的路由路径")
    private String activeMenu;

    @Schema(description = "设置为true即使hidden为true，也依然可以进行路由跳转(默认 false)")
    private Boolean canTo;

    @Schema(description = "['edit','add', 'delete']    设置该路由的权限")
    private String permission;

    @Schema(description = "子菜单")
    private List<MenuItem> children;

}
