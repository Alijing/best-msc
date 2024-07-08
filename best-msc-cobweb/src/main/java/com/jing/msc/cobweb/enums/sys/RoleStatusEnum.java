package com.jing.msc.cobweb.enums.sys;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @author : jing
 * @since : 2024/7/4 16:40
 */
@Getter
public enum RoleStatusEnum implements IEnum<Integer> {

    ENABLE(0, "可用"),
    DISABLE(1, "禁用");

    private final Integer value;
    private final String desc;

    RoleStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
