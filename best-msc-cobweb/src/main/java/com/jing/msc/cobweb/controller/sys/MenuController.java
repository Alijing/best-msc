package com.jing.msc.cobweb.controller.sys;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.sys.vo.CascaderDictItem;
import com.jing.msc.cobweb.entity.sys.vo.RoleMenuPerm;
import com.jing.msc.cobweb.entity.sys.vo.RouteRecord;
import com.jing.msc.cobweb.service.sys.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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
@RestController
@ApiSupport(order = 3)
@Tag(name = "菜单相关接口", description = "菜单相关接口描述")
@RequestMapping("/menu")
public class MenuController {

    @Resource(name = "menuService")
    private MenuService service;

    @WebLog(description = "新增菜单")
    @Operation(summary = "新增菜单")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping("/info")
    public BaseResp<Boolean> add(@RequestBody RouteRecord route) {
        return BaseResp.ok(service.addMenu(route));
    }

    @WebLog(description = "编辑菜单")
    @Operation(summary = "编辑菜单")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PutMapping("/info")
    public BaseResp<Boolean> edit(@RequestBody RouteRecord route) {
        return BaseResp.ok(service.editMenu(route));
    }

    @WebLog(description = "删除菜单")
    @Operation(summary = "删除菜单")
    @ApiOperationSupport(author = "Jing", order = 3)
    @DeleteMapping("/info")
    public BaseResp<Boolean> delete(@RequestParam(value = "ids", required = true) List<Long> ids) {
        return BaseResp.ok(service.deleteMenu(ids));
    }

    @WebLog(description = "获取当前用户的菜单")
    @Operation(summary = "获取当前用户的菜单")
    @ApiOperationSupport(author = "Jing", order = 4)
    @GetMapping("/current/user")
    public BaseResp<List<RouteRecord>> currentUserMenu() {
        return BaseResp.ok(service.currentUserMenu());
    }

    @WebLog(description = "查询所有菜单信息")
    @Operation(summary = "查询所有菜单信息")
    @ApiOperationSupport(author = "Jing", order = 5)
    @GetMapping("/info/list")
    public BaseResp<List<RouteRecord>> list(@RequestParam(value = "name", required = false) String name) {
        return BaseResp.ok(service.menuList(name));
    }

    @WebLog(description = "查询所有菜单简单信息")
    @Operation(summary = "查询所有菜单简单信息")
    @ApiOperationSupport(author = "Jing", order = 6)
    @GetMapping("/info/simple")
    public BaseResp<List<CascaderDictItem>> simpleList(@RequestParam(value = "all", required = false) boolean all,
                                                       @RequestParam(value = "current", required = false) boolean current,
                                                       @RequestParam(value = "permission", required = false) boolean permission) {
        return BaseResp.ok(service.simpleInfo(all, current, permission));
    }

    @WebLog(description = "查询当前登录人菜单权限")
    @Operation(summary = "查询当前登录人菜单权限")
    @ApiOperationSupport(author = "Jing", order = 7)
    @GetMapping("/current/permission")
    public BaseResp<List<RoleMenuPerm>> currentPermission() {
        return BaseResp.ok(service.currentPermission());
    }


    // ----------------------------------------------------------------------------------------------------------------

    @WebLog(description = "获取当前用户的菜单")
    @Operation(summary = "获取当前用户的菜单")
    @ApiOperationSupport(author = "Jing", order = 99)
    @GetMapping("/info/add")
    public BaseResp<Boolean> add() {
        service.initMenu();
        return BaseResp.ok();
    }


    @WebLog(description = "获取当前用户的菜单")
    @Operation(summary = "获取当前用户的菜单")
    @ApiOperationSupport(author = "Jing", order = 99)
    @GetMapping("/info/i18n")
    public BaseResp<Boolean> i18n() {
        service.initI18n();
        return BaseResp.ok();
    }


}

