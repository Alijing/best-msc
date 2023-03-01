package com.jing.msc.cobweb.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.entity
 * @date : 2021/11/12 10:07
 * @description :
 */
@TableName("novel_info")
public class Novel implements Serializable {
    private static final long serialVersionUID = 8964955463358958822L;
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 小说名称
     */
    private String name;
    /**
     * 章节 css 样式
     */
    private String chapterStyle;
    /**
     * 章节名称 css 样式
     */
    private String chapterValueStyle;
    /**
     * 章节下一页 css 样式
     */
    private String nextChapterStyle;
    /**
     * 章节下一页名称 css 样式
     */
    private String nextChapterValueStyle;
    /**
     * 小说地址
     */
    private String path;
    /**
     * 匹配规则
     */
    private String regex;
    /**
     * 内容 css 样式
     */
    private String contentStyle;
    /**
     * 内容下一页 css 样式
     */
    private String nextContentStyle;
    /**
     * 内容下一页链接 css 样式
     */
    private String nextContentValueStyle;
    /**
     * 爬取状态（0：未爬取，1：已爬取）
     */
    private Integer status;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifyTime;


    public Novel() {
    }


    public Novel(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
