package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统权限信息表
 * </p>
 *
 * @author jing
 * @since 2024-07-04 16:04:54
 */
@Data
@TableName("sys_menu_name")
@Schema(description = "系统菜单名称表")
public class MenuName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "菜单Id")
    @TableField("menu_id")
    private Long menuId;

    @Schema(description = "语言类型")
    @TableField("lang")
    private String lang;

    @Schema(description = "菜单名称")
    @TableField("name")
    private String name;


    @Override
    public String toString() {
        return "Menu{ name='" + name + " }";
    }

}
