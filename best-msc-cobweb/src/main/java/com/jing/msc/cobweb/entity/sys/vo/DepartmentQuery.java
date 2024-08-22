package com.jing.msc.cobweb.entity.sys.vo;

import com.jing.common.core.base.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.sys.vo
 * @date : 2023/11/10 16:04
 * @description :
 */
@Tag(name = "单位组织", description = "单位组织查询对象")
public class DepartmentQuery extends BasePage {

   @Schema(name = "上级Id")
    private List<Long> ids;

   @Schema(name = "上级Id")
    private Long parentId;

   @Schema(name = "单位组织名称")
    private String name;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
