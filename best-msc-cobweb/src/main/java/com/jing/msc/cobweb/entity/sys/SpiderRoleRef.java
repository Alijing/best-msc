package com.jing.msc.cobweb.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 系统用户与角色关联表
 * </p>
 *
 * @author jing
 * @since 2023-07-21 17:33:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_spider_role_ref")
@Tag(name = "系统用户", description = "系统用户与角色关联表")
public class SpiderRoleRef implements Serializable {

    private static final long serialVersionUID = -8198330381285410082L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "用户Id")
    @TableId(value = "spider_id", type = IdType.ASSIGN_ID)
    private Long spiderId;

    @Schema(name = "角色Id")
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private Long roleId;

    @Override
    public String toString() {
        return "SpiderRoleRef{" +
                "spiderId=" + spiderId +
                ", roleId=" + roleId +
                "}";
    }
}
