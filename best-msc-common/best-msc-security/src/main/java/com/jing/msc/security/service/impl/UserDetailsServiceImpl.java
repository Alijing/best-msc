package com.jing.msc.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jing.common.core.enums.ResultEnum;
import com.jing.msc.security.entity.LoginSpider;
import com.jing.msc.security.entity.Spider;
import com.jing.msc.security.mapper.MenuMapper;
import com.jing.msc.security.mapper.SpiderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.service.impl
 * @date : 2023/7/4 10:03
 * @description :
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SpiderMapper spiderMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<Spider> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spider::getAccount, userName);
        Spider spider = spiderMapper.selectOne(wrapper);
        if (Objects.isNull(spider)) {
            throw new RuntimeException(ResultEnum.User_Not_Found.getMessage());
        }
        List<String> permissions = menuMapper.selectPermsByUserId(spider.getId());
        spider.setPassword(null);
        return new LoginSpider(spider, permissions);
    }

}
