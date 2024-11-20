package com.jing.msc.cobweb.entity.sys.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/11/19 9:54
 */
@Data
@Tag(name = "用户信息", description = "用户信息实体")
public class SpiderInfo implements Serializable {

    private static final long serialVersionUID = 133638144532631288L;

    /**
     * 主键
     */
    @Schema(description = "用户Id")
    private Long id;
    /**
     * 登录账号
     */
    @Schema(description = "登录账号")
    private String account;
    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;
    /**
     * 角色Id
     */
    @Schema(description = "角色Id")
    private List<Long> roleId;
    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;
    /**
     * 部门Id
     */
    @Schema(description = "部门Id")
    private List<Long> departmentId;
    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String department;
    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
