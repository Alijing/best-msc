package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : jing
 * @since : 2024/11/18 16:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_spider_department_ref")
@Tag(name = "用户与部门关联表", description = "用户与部门关联表")
public class SpiderDepartmentRef implements Serializable {

    private static final long serialVersionUID = -8375283031365517955L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "用户Id")
    @TableField(value = "spider_id")
    private Long spiderId;

    @Schema(name = "角色Id")
    @TableField(value = "department_id")
    private Long departmentId;

}
