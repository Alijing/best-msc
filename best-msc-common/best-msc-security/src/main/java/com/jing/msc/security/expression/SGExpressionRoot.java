package com.jing.msc.security.expression;

import com.jing.msc.security.entity.LoginSpider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.expression
 * @date : 2023/10/13 15:42
 * @description :
 */
@Component("sgex")
public class SGExpressionRoot {

    public boolean hasAuthority(String authority) {
        // 获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginSpider loginSpider = (LoginSpider) authentication.getPrincipal();
        List<String> permissions = loginSpider.getPermissions();
        // 判断用户权限集合中是否包含 authority
        return permissions.contains(authority);
    }

}
