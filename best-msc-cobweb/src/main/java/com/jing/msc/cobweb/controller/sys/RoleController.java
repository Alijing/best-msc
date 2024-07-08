package com.jing.msc.cobweb.controller.sys;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.service.sys.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 系统角色信息表 前端控制器
 * </p>
 *
 * @author : jing
 * @since : 2024-07-04 16:21:35
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource(name = "roleService")
    private RoleService service;

    @WebLog(description = "查询角色信息")
    @ApiOperation(value = "查询角色信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/info")
    public BaseResp<IPage<Role>> list(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "current", required = false) Long current,
                                      @RequestParam(value = "size", required = false) Long size) {
        Role role = new Role();
        role.setName(name);
        return BaseResp.ok(service.roleList(role, current, size));
    }


}

