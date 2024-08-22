package com.jing.msc.cobweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author jing
 * @since 2023-03-31 09:41:11
 */
@TableName("novel_crawl_config")
@Tag(name = "小说模块", description = "小说爬虫配置信息表实体类")
public class NovelCrawlConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "小说Id")
    @TableField("novel_id")
    private Long novelId;

    @Schema(name = "内容爬取类型（0：网页爬取，1：API爬取）")
    @TableField("type")
    private Integer type;

    @Schema(name = "匹配规则")
    @TableField("regex")
    private String regex;

    @Schema(name = "章节条目样式")
    @TableField("chapter_style")
    private String chapterStyle;

    @Schema(name = "章节名称样式")
    @TableField("chapter_value_style")
    private String chapterValueStyle;

    @Schema(name = "下一页样式")
    @TableField("next_chapter_style")
    private String nextChapterStyle;

    @Schema(name = "下一页链接样式")
    @TableField("next_chapter_value_style")
    private String nextChapterValueStyle;

    @Schema(name = "章节内容样式")
    @TableField("content_style")
    private String contentStyle;

    @Schema(name = "内容下一页样式")
    @TableField("next_content_style")
    private String nextContentStyle;

    @Schema(name = "内容下一页链接样式")
    @TableField("next_content_value_style")
    private String nextContentValueStyle;

    @Schema(name = "爬取状态（0：未爬取，1：已爬取）")
    @TableField("status")
    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNovelId() {
        return novelId;
    }

    public void setNovelId(Long novelId) {
        this.novelId = novelId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getChapterStyle() {
        return chapterStyle;
    }

    public void setChapterStyle(String chapterStyle) {
        this.chapterStyle = chapterStyle;
    }

    public String getChapterValueStyle() {
        return chapterValueStyle;
    }

    public void setChapterValueStyle(String chapterValueStyle) {
        this.chapterValueStyle = chapterValueStyle;
    }

    public String getNextChapterStyle() {
        return nextChapterStyle;
    }

    public void setNextChapterStyle(String nextChapterStyle) {
        this.nextChapterStyle = nextChapterStyle;
    }

    public String getNextChapterValueStyle() {
        return nextChapterValueStyle;
    }

    public void setNextChapterValueStyle(String nextChapterValueStyle) {
        this.nextChapterValueStyle = nextChapterValueStyle;
    }

    public String getContentStyle() {
        return contentStyle;
    }

    public void setContentStyle(String contentStyle) {
        this.contentStyle = contentStyle;
    }

    public String getNextContentStyle() {
        return nextContentStyle;
    }

    public void setNextContentStyle(String nextContentStyle) {
        this.nextContentStyle = nextContentStyle;
    }

    public String getNextContentValueStyle() {
        return nextContentValueStyle;
    }

    public void setNextContentValueStyle(String nextContentValueStyle) {
        this.nextContentValueStyle = nextContentValueStyle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NovelCrawlConfig{" +
                "id=" + id +
                ", novelId=" + novelId +
                ", type=" + type +
                ", regex=" + regex +
                ", chapterStyle=" + chapterStyle +
                ", chapterValueStyle=" + chapterValueStyle +
                ", nextChapterStyle=" + nextChapterStyle +
                ", nextChapterValueStyle=" + nextChapterValueStyle +
                ", contentStyle=" + contentStyle +
                ", nextContentStyle=" + nextContentStyle +
                ", nextContentValueStyle=" + nextContentValueStyle +
                ", status=" + status +
                "}";
    }
}
