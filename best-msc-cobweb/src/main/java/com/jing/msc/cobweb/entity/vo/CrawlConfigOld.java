package com.jing.msc.cobweb.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.entity.vo
 * @date : 2023/3/31 11:29
 * @description :
 */
@Tag(name = "小说模块", description = "小说爬虫配置信息")
public class CrawlConfigOld {

    @Schema(name = "配置主键Id")
    private Long id;

    @Schema(name = "小说Id")
    private Long novelId;

    @Schema(name = "小说名称")
    private String name;

    @Schema(name = "小说地址")
    private String path;

    @Schema(name = "内容爬取类型（0：网页爬取，1：API爬取）")
    private Integer type;

    @Schema(name = "匹配规则")
    private String regex;

    @Schema(name = "章节条目样式")
    private String chapterStyle;

    @Schema(name = "章节名称样式")
    private String chapterValueStyle;

    @Schema(name = "下一页样式")
    private String nextChapterStyle;

    @Schema(name = "下一页链接样式")
    private String nextChapterValueStyle;

    @Schema(name = "章节内容样式")
    private String contentStyle;

    @Schema(name = "内容下一页样式")
    private String nextContentStyle;

    @Schema(name = "内容下一页链接样式")
    private String nextContentValueStyle;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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


    @Override
    public String toString() {
        return "CrawlConfig{" +
                "id=" + id +
                ", novelId=" + novelId +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", regex='" + regex + '\'' +
                ", chapterStyle='" + chapterStyle + '\'' +
                ", chapterValueStyle='" + chapterValueStyle + '\'' +
                ", nextChapterStyle='" + nextChapterStyle + '\'' +
                ", nextChapterValueStyle='" + nextChapterValueStyle + '\'' +
                ", contentStyle='" + contentStyle + '\'' +
                ", nextContentStyle='" + nextContentStyle + '\'' +
                ", nextContentValueStyle='" + nextContentValueStyle + '\'' +
                '}';
    }
}
