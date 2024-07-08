package com.jing.msc.cobweb.enums.sys;

import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author : jing
 * @since : 2024/7/4 17:20
 */
@Getter
public enum RoleEnum {
    ADMIN(1L, "ADMIN", "超级管理员"),
    NORMAL_USER(2L, "NORMAL_USER", "普通用户");

    private final Long id;
    private final String code;
    private final String name;

    RoleEnum(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static long getIdByCode(String code) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum.getId();
            }
        }
        throw new CustomException(ResultEnum.ROLE_NOT_FOUND);
    }

    public static boolean hasRoleByCode(RoleEnum target, List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return false;
        }
        for (String code : codes) {
            if (target.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasRoleById(RoleEnum target, List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        for (Long id : ids) {
            if (target.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


}
