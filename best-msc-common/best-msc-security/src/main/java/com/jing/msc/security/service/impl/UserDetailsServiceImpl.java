package com.jing.msc.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.base.BaseResp;
import com.jing.common.core.constant.Constants;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.RedisUtils;
import com.jing.msc.security.entity.LoginSpider;
import com.jing.msc.security.entity.Spider;
import com.jing.msc.security.entity.TokenInfo;
import com.jing.msc.security.mapper.SpiderMapper;
import com.jing.msc.security.service.SecurityUserService;
import com.jing.msc.security.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.service.impl
 * @date : 2023/7/4 10:03
 * @description :
 */
@Service(value = "userDetailsService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
public class UserDetailsServiceImpl extends ServiceImpl<SpiderMapper, Spider> implements UserDetailsService, SecurityUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource(name = "redisUtils")
    private RedisUtils redisUtils;

    @Override
    public BaseResp<TokenInfo> login(Spider spider) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(spider.getAccount(), spider.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new CustomException(ResultEnum.Login_Failure);
        }
        LoginSpider loginSpider = (LoginSpider) authenticate.getPrincipal();
        Long id = loginSpider.getSpider().getId();
        String jwt = JwtUtil.createJWT(id.toString());
        try {
            String compress = JwtUtil.compress(jwt);
            logger.info("jwt compress :{}", compress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //redisUtils.set(Constants.LOGIN_USER_KEY + id, JsonUtils.toJson(loginSpider));
        return BaseResp.ok(new TokenInfo(jwt, Constants.TOKEN_EXPIRES_TIME));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<Spider> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spider::getAccount, userName);
        Spider spider = baseMapper.selectOne(wrapper);
        if (Objects.isNull(spider)) {
            throw new RuntimeException(ResultEnum.User_Not_Found.getMessage());
        }
        List<String> roles = baseMapper.selectRoleByUserId(spider.getId());
        List<String> permissions = baseMapper.selectPermsByUserId(spider.getId());
        LoginSpider loggedIn = new LoginSpider(spider, permissions);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        loggedIn.setAuthorities(authorities);
        return loggedIn;
    }

    @Override
    public BaseResp<Boolean> logout() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginSpider loginSpider = (LoginSpider) authenticationToken.getPrincipal();
        redisUtils.delete(Constants.LOGIN_USER_KEY + loginSpider.getSpider().getId());
        return BaseResp.ok(true);
    }

}
