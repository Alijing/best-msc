package com.jing.msc.cobweb.controller.sys;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BasePageResp;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.entity.sys.vo.MenuSimpleInfo;
import com.jing.msc.cobweb.entity.sys.vo.RoleInfo;
import com.jing.msc.cobweb.service.sys.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系统角色信息表 前端控制器
 * </p>
 *
 * @author : jing
 * @since : 2024-07-04 16:21:35
 */
@RestController
@ApiSupport(order = 2)
@Tag(name = "角色相关接口", description = "角色相关接口描述")
@RequestMapping("/sys/role")
public class RoleController {

    @Resource(name = "roleService")
    private RoleService service;

    @WebLog(description = "查询角色简单信息")
    @Operation(summary = "查询角色简单信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    //@PreAuthorize("@sgex.hasAuthority('sys:department:list')")
    @GetMapping("/simple/list")
    public BaseResp<List<Role>> simpleList() {
        List<Role> nodes = service.simpleList();
        if (CollectionUtils.isEmpty(nodes)) {
            return BaseResp.ok(new ArrayList<>());
        }
        return BaseResp.ok(nodes);
    }

    @WebLog(description = "查询角色信息")
    @Operation(summary = "查询角色信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/info/list")
    public BaseResp<List<Role>> list(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "pageIndex") Long current,
                                     @RequestParam(value = "pageSize") Long size) {
        Role role = new Role();
        role.setName(name);
        IPage<Role> ret = service.roleList(role, current, size);
        return BasePageResp.ok(ret.getRecords(), ret.getTotal(), current, size);
    }

    @WebLog(description = "通过角色Id查询角色信息")
    @Operation(summary = "通过角色Id查询角色信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/info/{id}")
    public BaseResp<RoleInfo> list(@PathVariable("id") Long id) {
        Assert.notNull(id, "角色id不能为空");
        RoleInfo ret = service.roleInfo(id);
        return BaseResp.ok(ret);
    }

    @WebLog(description = "获取当前用户的菜单")
    @Operation(summary = "获取当前用户的菜单")
    @ApiOperationSupport(author = "Jing", order = 4)
    @GetMapping("/menu/ref/{roleId}")
    public BaseResp<List<MenuSimpleInfo>> menuRoleRef(@PathVariable("roleId") Long roleId,
                                                      @RequestParam(value = "permission", required = false) Boolean permission) {
        Assert.notNull(roleId, "角色id不能为空");
        return BaseResp.ok(service.roleMenuRef(roleId, permission));
    }

    @WebLog(description = "新增角色信息")
    @Operation(summary = "新增角色信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("/info")
    public BaseResp<Long> add(@Valid @RequestBody RoleInfo target) {
        Long roleId = service.saveOrUpdate(target);
        if (Objects.isNull(roleId)) {
            return BaseResp.error("更新失败");
        }
        return BaseResp.ok(roleId);
    }

    @WebLog(description = "修改角色信息")
    @Operation(summary = "修改角色信息")
    @ApiOperationSupport(author = "Jing", order = 3)
    @PutMapping("/info")
    public BaseResp<Long> edit(@Valid @RequestBody RoleInfo target) {
        Long roleId = service.saveOrUpdate(target);
        if (Objects.isNull(roleId)) {
            return BaseResp.error("更新失败");
        }
        return BaseResp.ok(roleId);
    }

    @WebLog(description = "删除角色信息")
    @Operation(summary = "删除角色信息")
    @ApiOperationSupport(author = "Jing", order = 4)
    @DeleteMapping("/info")
    public BaseResp<Boolean> delete(@RequestParam(value = "ids") List<Long> ids) {
        boolean delete = service.delete(ids);
        return delete ? BaseResp.ok(true) : BaseResp.error("删除失败");
    }


    @WebLog(description = "校验角色编码是否唯一")
    @Operation(summary = "校验角色编码是否唯一")
    @ApiOperationSupport(author = "Jing", order = 5)
    @GetMapping("/code/valid")
    public BaseResp<Boolean> delete(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "value") String code) {
        boolean unique = service.codeValid(id, code);
        return unique ? BaseResp.ok(true) : BaseResp.ok(false, "角色编码【" + code + "】已存在");
    }

}

