package com.jing.msc.cobweb.entity.crawl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author : jing
 * @since : 2024/12/17 17:52
 */
@Data
@Schema(description = "爬取策略")
public class CrawlSchema {

    @Schema(description = "地址")
    private String url;

    @Schema(description = "返回类型（0：列表，1：文本）")
    private Integer retType;

    @Schema(description = "爬取模式（0：网页爬取，1：API爬取）")
    private Integer mode;

    @Schema(description = "匹配规则")
    private String regex;

    @Schema(description = "策略名称")
    private String name;

    @Schema(description = "基础选择器")
    private String baseSelector;

    @Schema(description = "字段列表")
    private List<CrawlField> fields;

}
