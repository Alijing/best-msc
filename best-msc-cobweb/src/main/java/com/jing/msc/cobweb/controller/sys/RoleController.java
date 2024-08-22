package com.jing.msc.cobweb.controller.sys;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.service.sys.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 系统角色信息表 前端控制器
 * </p>
 *
 * @author : jing
 * @since : 2024-07-04 16:21:35
 */
@Tag(name = "角色相关接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource(name = "roleService")
    private RoleService service;

    @WebLog(description = "查询角色信息")
    @Operation(summary = "查询角色信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/info/list")
    public BaseResp<IPage<Role>> list(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "current", required = false) Long current,
                                      @RequestParam(value = "size", required = false) Long size) {
        Role role = new Role();
        role.setName(name);
        return BaseResp.ok(service.roleList(role, current, size));
    }

    @WebLog(description = "新增角色信息")
    @Operation(summary = "新增角色信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("/info")
    public BaseResp<IPage<Role>> add(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "current", required = false) Long current,
                                     @RequestParam(value = "size", required = false) Long size) {
        Role role = new Role();
        role.setName(name);
        return BaseResp.ok(service.roleList(role, current, size));
    }

    @WebLog(description = "修改角色信息")
    @Operation(summary = "修改角色信息")
    @ApiOperationSupport(author = "Jing", order = 3)
    @PutMapping("/info")
    public BaseResp<IPage<Role>> edit(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "current", required = false) Long current,
                                      @RequestParam(value = "size", required = false) Long size) {
        Role role = new Role();
        role.setName(name);
        return BaseResp.ok(service.roleList(role, current, size));
    }

    @WebLog(description = "删除角色信息")
    @Operation(summary = "删除角色信息")
    @ApiOperationSupport(author = "Jing", order = 4)
    @DeleteMapping("/info")
    public BaseResp<IPage<Role>> delete(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "current", required = false) Long current,
                                        @RequestParam(value = "size", required = false) Long size) {
        Role role = new Role();
        role.setName(name);
        return BaseResp.ok(service.roleList(role, current, size));
    }


}

