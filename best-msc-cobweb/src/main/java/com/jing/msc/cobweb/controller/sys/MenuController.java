package com.jing.msc.cobweb.controller.sys;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.sys.vo.RouteRecord;
import com.jing.msc.cobweb.service.sys.MenuService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统权限信息表 前端控制器
 * </p>
 *
 * @author : jing
 * @since : 2024-07-04 16:04:54
 */
@Tag(name = "菜单相关接口")
@RestController
@RequestMapping("/menu")
public class MenuController {


    @Resource(name = "menuService")
    private MenuService service;

    @WebLog(description = "获取当前用户的菜单")
    @ApiOperation(value = "获取当前用户的菜单")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/current/user")
    public BaseResp<List<RouteRecord>> currentUserMenu() {
        return BaseResp.ok(service.currentUserMenu());
    }

    @WebLog(description = "获取当前用户的菜单")
    @ApiOperation(value = "获取当前用户的菜单")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/info/add")
    public BaseResp<Boolean> add() {
        service.initMenu();
        return BaseResp.ok();
    }


}

