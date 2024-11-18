package com.jing.msc.cobweb.controller.sys;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BasePageResp;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.sys.Department;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentNode;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentQuery;
import com.jing.msc.cobweb.service.sys.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author : jing
 * @packageName : com.fullsee.integratedbis.controller
 * @description :
 * @since : 2023-10-16 20:52:26
 */
@RestController
@ApiSupport(order = 3)
@Tag(name = "部门相关接口", description = "部门相关接口描述")
@RequestMapping("/sys/department")
public class DepartmentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "departmentService")
    private DepartmentService service;

    @WebLog(description = "查询单位组织信息")
    @Operation(summary = "查询单位组织信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    //@PreAuthorize("@sgex.hasAuthority('sys:department:list')")
    @PostMapping("/list")
    public BaseResp<List<DepartmentNode>> list(@RequestBody DepartmentQuery query) {
        IPage<DepartmentNode> departments = service.departmentTree(query);
        if (CollectionUtils.isEmpty(departments.getRecords())) {
            return BaseResp.ok();
        }
        return BasePageResp.ok(departments.getRecords(), departments.getTotal(), departments.getCurrent(), departments.getSize());
    }

    @WebLog(description = "查询单位组织简单信息")
    @Operation(summary = "查询单位组织简单信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    //@PreAuthorize("@sgex.hasAuthority('sys:department:list')")
    @GetMapping("/simple/list")
    public BaseResp<List<DepartmentNode>> simpleList() {
        List<DepartmentNode> departments = service.simpleList();
        if (CollectionUtils.isEmpty(departments)) {
            return BaseResp.ok(new ArrayList<>());
        }
        return BaseResp.ok(departments);
    }


    @WebLog(description = "新增或编辑单位组织信息")
    @Operation(summary = "新增或编辑单位组织信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    //@PreAuthorize("@sgex.hasAuthority('sys:department:update')")
    @PostMapping("/update")
    public BaseResp<Long> update(@RequestBody Department department) {
        boolean update = service.saveOrUpdate(department);
        if (update) {
            return BaseResp.ok(department.getId());
        }
        return BaseResp.error("更新失败");
    }

    @WebLog(description = "删除单位组织信息")
    @Operation(summary = "删除单位组织信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    //@PreAuthorize("@sgex.hasAuthority('sys:department:del')")
    @PostMapping("/del")
    public BaseResp<Long> del(@RequestBody DepartmentQuery query) {
        if (CollectionUtils.isEmpty(query.getIds())) {
            return BaseResp.ok();
        }
        service.removeByIds(query.getIds());
        return BaseResp.ok();
    }

}

