package com.jing.msc.cobweb.enums.crawl;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据类型，0：小说目录，1：小说文本
 * @author : jing
 * @since : 2024/12/18 11:41
 */
@Getter
@AllArgsConstructor
public enum DataType implements IEnum<Integer> {
    NOVEL_CHAPTER(0, "小说目录"),
    NOVEL_CONTENT(1, "小说文本"),
    ;


    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String label;

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getLabel();
    }


}
