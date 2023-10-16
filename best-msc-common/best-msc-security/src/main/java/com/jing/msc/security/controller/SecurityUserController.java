package com.jing.msc.security.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.security.entity.Spider;
import com.jing.msc.security.service.SecurityUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.security.controller
 * @date : 2023/7/4 13:57
 * @description :
 */
@RestController
@RequestMapping("security/user/")
public class SecurityUserController {

    @Resource(name = "securityUserService")
    private SecurityUserService userService;

    @WebLog(description = "用户登录接口")
    @ApiOperation(value = "用户登录接口")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping(value = "login")
    public BaseResp<String> login(@RequestBody Spider spider) {
        return userService.login(spider);
    }

    @WebLog(description = "用户注销接口")
    @ApiOperation(value = "用户注销接口")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping(value = "logout")
    public BaseResp<Boolean> logout() {
        return userService.logout();
    }


}
