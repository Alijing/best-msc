package com.jing.msc.cobweb.enums.crawl;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : jing
 * @since : 2024/12/18 11:42
 */
@Getter
@AllArgsConstructor
public enum Mode implements IEnum<Integer> {

    WEB(0, "网页爬取"),
    API(1, "API爬取"),
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
