package com.jing.msc.cobweb.entity.sys.vo;

import com.jing.common.core.base.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.sys.vo
 * @date : 2023/11/10 16:04
 * @description :
 */
@ApiModel(value = "Department 查询对象", description = "")
public class DepartmentQuery extends BasePage {

    @ApiModelProperty("上级Id")
    private List<Long> ids;

    @ApiModelProperty("上级Id")
    private Long parentId;

    @ApiModelProperty("单位组织名称")
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
