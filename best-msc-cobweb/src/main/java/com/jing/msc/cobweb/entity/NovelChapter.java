package com.jing.msc.cobweb.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.entity
 * @date : 2021/11/11 17:45
 * @description :
 */
@TableName("novel_chapter")
public class NovelChapter implements Serializable {

    /**
     * 主键Id
     */
    private Long id;
    /**
     * 章节名称
     */
    private String name;
    /**
     * 章节路径
     */
    private String path;
    /**
     * 所属小说
     */
    private Long novelId;
    /**
     * 章节内容状态 0：未拉取，1：已拉取
     */
    private Integer status;
    /**
     * 内容 css 样式
     */
    private String contentStyle;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String modifyTime;


    public NovelChapter() {
    }

    public NovelChapter(Long id, String name, String path, Long novelId,String contentStyle) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.novelId = novelId;
        this.contentStyle = contentStyle;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getNovelId() {
        return novelId;
    }

    public void setNovelId(Long novelId) {
        this.novelId = novelId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContentStyle() {
        return contentStyle;
    }

    public void setContentStyle(String contentStyle) {
        this.contentStyle = contentStyle;
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
