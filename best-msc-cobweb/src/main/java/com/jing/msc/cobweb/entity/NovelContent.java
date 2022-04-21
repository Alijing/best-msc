package com.jing.msc.cobweb.entity;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.entity
 * @date : 2021/11/12 14:50
 * @description :
 */
public class NovelContent {
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 章节Id
     */
    private Long chapterId;
    /**
     * 章节内容
     */
    private String content;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String modifyTime;

    public NovelContent() {
    }

    public NovelContent(Long chapterId, String content) {
        this.chapterId = chapterId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
