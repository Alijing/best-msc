package com.jing.msc.cobweb.enums.sys;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : jing
 * @since : 2024/7/4 16:40
 */
@Getter
@AllArgsConstructor
public enum RoleStatusEnum implements IEnum<Integer> {

    ENABLE(0, "启用"),
    DISABLE(1, "禁用");

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
