package com.jing.msc.cobweb.entity.sys.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jing.msc.cobweb.enums.sys.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : jing
 * @since : 2024/7/4 16:34
 */
@Data
@Tag(name = "系统角色", description = "角色查询返回实体类")
public class RoleQueryRet {

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


}
