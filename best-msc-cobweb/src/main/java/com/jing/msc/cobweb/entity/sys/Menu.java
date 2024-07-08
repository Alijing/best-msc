package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统权限信息表
 * </p>
 *
 * @author jing
 * @since 2024-07-04 16:04:54
 */
@Data
@TableName("sys_menu")
@Schema(description = "系统权限信息表")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "上级菜单Id，当该节点为根节点时该字段值为0")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题")
    @TableField("name")
    private String name;

    @Schema(description = "菜单路径")
    @TableField("path")
    private String path;

    @Schema(description = "noredirect，当设置 noredirect 的时候该路由在面包屑导航中不可被点击")
    @TableField("redirect")
    private String redirect;

    @Schema(description = "前端组件地址")
    @TableField("component")
    private String component;

    @Schema(description = "路由在侧边栏和面包屑中展示的名字")
    @TableField("title")
    private String title;

    @Schema(description = "当设置 true 的时候该路由不会再侧边栏出现 如404，login等页面(默认 false)")
    @TableField("hidden")
    private Integer hidden;

    @Schema(description = "当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式，只有一个时，会将那个子路由当做根路由显示在侧边栏，若你想不管路由下面的 children 声明的个数都显示你的根路由， 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，	一直显示根路由(默认 false)")
    @TableField("always_show")
    private Integer alwaysShow;

    @Schema(description = "路由的图标")
    @TableField("icon")
    private String icon;

    @Schema(description = "如果设置为true，则不会被 <keep-alive> 缓存(默认 false)")
    @TableField("no_cache")
    private Integer noCache;

    @Schema(description = "如果设置为false，则不会在breadcrumb面包屑中显示(默认 true)")
    @TableField("breadcrumb")
    private Integer breadcrumb;

    @Schema(description = "如果设置为true，则会一直固定在tag项中(默认 false)")
    @TableField("affix")
    private Integer affix;

    @Schema(description = "如果设置为true，则不会出现在tag中(默认 false)")
    @TableField("no_tags_view")
    private Integer noTagsView;

    @Schema(description = "显示高亮的路由路径")
    @TableField("active_menu")
    private String activeMenu;

    @Schema(description = "设置为true即使hidden为true，也依然可以进行路由跳转(默认 false)")
    @TableField("can_to")
    private Integer canTo;

    @Schema(description = "['edit','add', 'delete']    设置该路由的权限")
    @TableField("permission")
    private String permission;

    @Schema(description = "权限key")
    @TableField("perm_key")
    private String permKey;

    @Schema(description = "状态，0：可用，1：禁用")
    @TableField("status")
    private Integer status;

    @Schema(description = "创建人Id")
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改人Id")
    @TableField(value = "reviser_id", fill = FieldFill.INSERT_UPDATE)
    private Long reviserId;

    @Schema(description = "修改时间")
    @TableField(value = "revision_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime revisionTime;

    @Schema(description = "数据逻辑标识（0：正常，1：已删除）默认：0")
    @TableField("logic_flag")
    private Integer logicFlag;

    @Schema(description = "乐观锁")
    @TableField("version")
    private Integer version;

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

}
