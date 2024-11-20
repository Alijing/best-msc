package com.jing.msc.cobweb.entity.sys.vo;

import com.jing.common.core.base.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.sys.vo
 * @date : 2023/11/10 16:04
 * @description :
 */
@Data
@Tag(name = "单位组织", description = "单位组织查询对象")
public class DepartmentQuery extends BasePage {

   @Schema(name = "上级Id")
    private List<Long> ids;

   @Schema(name = "上级Id")
    private Long parentId;

   @Schema(name = "单位组织名称")
    private String name;

}
