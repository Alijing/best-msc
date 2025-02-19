package com.jing.msc.cobweb.entity.crawl;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.jing.msc.cobweb.entity.crawl.dto.CrawlField;
import com.jing.msc.cobweb.enums.crawl.Mode;
import com.jing.msc.cobweb.enums.crawl.RetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 爬取配置
 * </p>
 *
 * @author jing
 * @since 2024-12-18 11:35:10
 */
@Data
@TableName("crawl_config")
@Schema(description = "爬取配置")
public class CrawlConfig implements Serializable {

    private static final long serialVersionUID = 8526457095301686188L;

    @Schema(description = "自增主键")
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "小说Id")
    @TableField("`novel_id`")
    private Long novelId;

    @Schema(description = "返回类型（0：列表，1：文本）")
    @TableField("`ret_type`")
    private RetType retType;

    @Schema(description = "爬取模式（0：网页爬取，1：API爬取）")
    @TableField("`mode`")
    private Mode mode;

    @Schema(description = "匹配规则")
    @TableField("`regex`")
    private String regex;

    @Schema(description = "策略名称")
    @TableField("`name`")
    private String name;

    @Schema(description = "基础CSS选择器")
    @TableField("`base_selector`")
    private String baseSelector;

    @Schema(description = "字段列表，当 ret_type = 0 时该字段有意义，返回的列表项字段信息")
    @TableField(value = "`fields`", typeHandler = Fastjson2TypeHandler.class)
    private List<CrawlField> fields;

    @Schema(description = "创建者用户Id")
    @TableField(value = "`gmt_creator`", fill = FieldFill.INSERT)
    private Long gmtCreator;

    @Schema(description = "创建时间")
    @TableField(value = "`gmt_create`", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @Schema(description = "更新时间")
    @TableField(value = "`gmt_modified`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
