package com.jing.msc.cobweb.entity.sys.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jing.msc.cobweb.enums.sys.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/11/21 10:40
 */
@Data
@Schema(description = "角色信息")
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = -3227420540821467196L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "状态：0：可用，1：禁用")
    private RoleStatusEnum status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "授权的菜单")
    private List<Long> menus;

    @Schema(description = "授权的菜单")
    private List<RoleMenuPerm> permission;

}
