package com.jing.msc.security.handler;

import com.alibaba.fastjson.JSON;
import com.jing.common.core.base.BaseResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.handler
 * @date : 2023/10/11 17:09
 * @description :
 */
@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        try {
            BaseResp<Object> error = BaseResp.error(HttpStatus.UNAUTHORIZED.value(), "用户认证失败请重新登录");
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(error));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        try {
            BaseResp<Object> error = BaseResp.error(HttpStatus.FORBIDDEN.value(), "您的权限不足，请联系管理员授权");
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(error));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
