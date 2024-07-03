package com.jing.msc.security.service.impl;

import com.jing.common.core.base.BaseResp;
import com.jing.common.core.constant.Constants;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.JsonUtils;
import com.jing.msc.security.entity.LoginSpider;
import com.jing.msc.security.entity.Spider;
import com.jing.msc.security.service.SecurityUserService;
import com.jing.msc.security.utils.JwtUtil;
import com.jing.msc.security.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.service.impl
 * @date : 2023/7/4 14:07
 * @description :
 */
@Service(value = "securityUserService")
public class SecurityUserServiceImpl implements SecurityUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource(name = "redisUtils")
    private RedisUtils redisUtils;

    @Override
    public BaseResp<String> login(Spider spider) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(spider.getAccount(), spider.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new CustomException(ResultEnum.Login_Failure);
        }
        LoginSpider loginSpider = (LoginSpider) authenticate.getPrincipal();
        Long id = loginSpider.getSpider().getId();
        String jwt = JwtUtil.createJWT(id.toString());
        redisUtils.set(Constants.LOGIN_USER_KEY + id, JsonUtils.toJson(loginSpider));
        return BaseResp.ok(jwt);
    }

    @Override
    public BaseResp<Boolean> logout() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginSpider loginSpider = (LoginSpider) authenticationToken.getPrincipal();
        redisUtils.delete(Constants.LOGIN_USER_KEY + loginSpider.getSpider().getId());
        return BaseResp.ok(true);
    }


}
