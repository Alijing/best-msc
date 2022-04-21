package com.jing.msc.cobweb.enums;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.enums
 * @date : 2021/11/12 11:23
 * @description :
 */
public enum MagicEnum {
    /**
     * 小说章节
     */
    NOVEL_CHAPTER("NOVEL_CHAPTER", "爬取的小说章节"),
    NOVEL_CONTENT("NOVEL_CONTENT", "爬取的小说章节内容"),
    VIDEO("VIDEO", "中间件无需与下游系统交互"),
    ;


    /**
     * 主键
     */
    private final String key;
    /**
     * 描述
     */
    private final String desc;

    MagicEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
