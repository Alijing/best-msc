package com.jing.msc.security.utils;

import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.msc.security.entity.LoginSpider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 当前登录用户信息工具类
 *
 * @author : jing
 * @since : 2024/11/20 10:18
 */
public class UserUtil {


    /**
     * 获取当前登录用户信息
     *
     * @return {@link LoginSpider }
     */
    public static LoginSpider getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new CustomException(ResultEnum.UN_Login);
        }
        return (LoginSpider) authentication.getPrincipal();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return {@link Long }
     */
    public static Long getUserId() {
        return getUserInfo().getSpider().getId();
    }


    /**
     * 获取当前登录用户姓名
     *
     * @return {@link String }
     */
    public static String getUserName() {
        return getUserInfo().getSpider().getName();
    }

    /**
     * 获取当前登录用户账号
     *
     * @return {@link String }
     */
    public static String getUserAccount() {
        return getUserInfo().getSpider().getAccount();
    }

    /**
     * 获取当前登录用户角色编码
     *
     * @return {@link String }
     */
    @SuppressWarnings("unchecked")
    public static List<String> getRole() {
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) getUserInfo().getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return new ArrayList<>();
        }
        return authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    /**
     * 判断当前登录用户是否拥有指定角色
     *
     * @param roleCode 角色编码
     * @return boolean
     */
    public static boolean hasRole(String roleCode) {
        List<String> roles = getRole();
        return roles.contains(roleCode);
    }

    /**
     * 获取当前登录用户语言
     *
     * @return {@link String }
     */
    public static String getLang() {
        LoginSpider userInfo = getUserInfo();
        return Objects.isNull(userInfo.getLang()) ? "zh-CN" : userInfo.getLang();
    }

}
