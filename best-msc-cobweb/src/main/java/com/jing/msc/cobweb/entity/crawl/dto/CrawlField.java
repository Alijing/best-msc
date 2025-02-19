package com.jing.msc.cobweb.entity.crawl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : jing
 * @since : 2024/12/18 14:36
 *<p/>
 *{
 *     "baseSelector": ".product",
 *     "fields": [
 *         {
 *             "name": "title",
 *             "selector": ".title",
 *             "type": "text"
 *         },
 *         {
 *             "name": "image_url",
 *             "selector": ".product-image",
 *             "type": "attribute",
 *             "attribute": "src"
 *         },
 *         {
 *             "name": "price",
 *             "selector": ".price",
 *             "type": "regex",
 *             "pattern": "^.{1,}$"
 *         },
 *         {
 *             "name": "description_html",
 *             "selector": ".description",
 *             "type": "html"
 *         }
 *     ]
 * }
 */
@Data
@Schema(description = "采集字段")
public class CrawlField {

    @Schema(description = "字段key")
    private String name;

    @Schema(description = "字段CSS")
    private String selector;

    @Schema(description = "字段类型，" +
            "- text: 提取可见文本.\n" +
            "- attribute:  捕获HTML属性（例如，src, href）.\n" +
            "- html: 提取元素的原始HTML.\n" +
            "- regex: 允许使用正则表达式模式提取文本的一部分. " +
            "Explanation:\n" +
            "- attribute: 从 .product-image 提取 src 属性.\n" +
            "- regex: 提取 $19.99 中的数值部分.\n" +
            "- html: 获取.description元素的完整HTML.")
    private String type;

    @Schema(description = "字段")
    private String attribute;

    @Schema(description = "字段")
    private String pattern;

}
