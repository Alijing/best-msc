package com.jing.msc.cobweb.entity.sys.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.sys.vo
 * @date : 2023/11/10 14:43
 * @description :
 */
@Data
@Tag(name = "单位组织", description = "单位组织节点实体类")
public class DepartmentNode {

    @Schema(name = "自增主键")
    private Long id;

    @Schema(name = "上级Id")
    private Long parentId;

    @Schema(name = "上级部门名称")
    private String parentName;

    @Schema(name = "部门名称")
    private String name;

    @Schema(name = "状态，0：启用，1禁用")
    private Integer status;

    @Schema(name = "备注")
    private String remarks;

    @Schema(name = "创建人Id")
    private Long creatorId;

    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(name = "修改人Id")
    private Long reviserId;

    @Schema(name = "修改时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime revisionTime;

    private List<DepartmentNode> children;

}
