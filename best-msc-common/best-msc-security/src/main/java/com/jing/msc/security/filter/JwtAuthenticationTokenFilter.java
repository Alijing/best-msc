package com.jing.msc.security.filter;

import com.jing.common.core.constant.Constants;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.JsonUtils;
import com.jing.msc.security.entity.LoginSpider;
import com.jing.msc.security.utils.JwtUtil;
import com.jing.msc.security.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.filter
 * @date : 2023/7/4 17:39
 * @description :
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "redisUtils")
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CustomException(ResultEnum.Token_Illegal);
        }
        String loginSpider = redisUtils.get(Constants.LOGIN_USER_KEY + userId);
        if (Objects.isNull(loginSpider)) {
            throw new RuntimeException(ResultEnum.UN_Login.getMessage());
        }
        LoginSpider spider = JsonUtils.toBean(loginSpider, LoginSpider.class);
        // TODO 获取权限信息到  Authentication  中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginSpider, null, spider.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
