package com.jing.msc.cobweb.enums.crawl;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : jing
 * @since : 2024/12/18 11:41
 */
@Getter
@AllArgsConstructor
public enum RetType implements IEnum<Integer> {
    LIST(0, "列表"),
    TEXT(1, "文本"),
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
