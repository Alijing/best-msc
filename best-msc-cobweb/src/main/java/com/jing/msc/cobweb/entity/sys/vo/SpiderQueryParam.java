package com.jing.msc.cobweb.entity.sys.vo;

import com.jing.common.core.base.BasePage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : jing
 * @since : 2024/11/18 15:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Tag(name = "系统菜单", description = "前端路由实体")
public class SpiderQueryParam extends BasePage {

    private Long departmentId;

    private String name;

    private String account;

}
