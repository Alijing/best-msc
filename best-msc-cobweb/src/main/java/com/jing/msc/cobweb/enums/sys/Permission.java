package com.jing.msc.cobweb.enums.sys;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作权限枚举
 *
 * @author : jing
 * @since : 2024/11/21 15:11
 */
@Getter
@AllArgsConstructor
public enum Permission implements IEnum<String> {

    QUERY("query", "查询"),
    ADD("add", "新增"),
    MODIFY("modify", "修改"),
    DELETE("delete", "删除");

    /**
     * 权限值
     */
    private final String value;

    /**
     * 权限描述
     */
    private final String label;

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getLabel();
    }

    public static Permission getByValue(String value) {
        for (Permission permission : Permission.values()) {
            if (permission.getValue().equals(value)) {
                return permission;
            }
        }
        return null;
    }

}
