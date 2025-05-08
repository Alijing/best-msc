package com.jing.msc.cobweb.enums.video;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : jing
 * @since : 2025/5/6 16:06
 */
@Getter
@AllArgsConstructor
public enum VideoStatusEnum implements IEnum<Integer> {

    NOT_DOWNLOADED(0, "未下载"),
    DOWNLOADED(1, "已下载"),
    VIEWED(2, "已观看"),
    ;

    private final Integer value;

    private final String desc;

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getDesc();
    }





}
