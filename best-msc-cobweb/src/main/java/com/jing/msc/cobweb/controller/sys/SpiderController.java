package com.jing.msc.cobweb.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BasePageResp;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.sys.vo.SpiderInfo;
import com.jing.msc.cobweb.entity.sys.vo.SpiderQueryParam;
import com.jing.msc.cobweb.service.sys.SpiderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/11/18 15:09
 */
@RestController
@ApiSupport(order = 2)
@Tag(name = "用户信息接口", description = "用户信息相关接口描述")
@RequestMapping("/sys/spider")
public class SpiderController {

    @Resource(name = "spiderService")
    private SpiderService service;

    @WebLog(description = "分页查询用户信息")
    @Operation(summary = "分页查询用户信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/list")
    public BaseResp<List<SpiderInfo>> list(@ModelAttribute SpiderQueryParam query) {
        IPage<SpiderInfo> pages = service.list(query);
        if (CollectionUtils.isEmpty(pages.getRecords())) {
            return BaseResp.ok();
        }
        return BasePageResp.ok(pages.getRecords(), pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @WebLog(description = "新增用户信息")
    @Operation(summary = "新增用户信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("/info")
    public BaseResp<Boolean> add(@RequestBody SpiderInfo target) {
        boolean update = service.spiderSaveOrUpdate(target);
        return update ? BaseResp.ok(true) : BaseResp.error("更新失败");
    }

    @WebLog(description = "修改用户信息")
    @Operation(summary = "修改用户信息")
    @ApiOperationSupport(author = "Jing", order = 3)
    @PutMapping("/info")
    public BaseResp<Boolean> edit(@RequestBody SpiderInfo target) {
        boolean update = service.spiderSaveOrUpdate(target);
        return update ? BaseResp.ok(true) : BaseResp.error("更新失败");
    }

    @WebLog(description = "删除用户")
    @Operation(summary = "删除用户")
    @ApiOperationSupport(author = "Jing", order = 4)
    @DeleteMapping("/info")
    public BaseResp<Boolean> delete(@RequestParam(value = "ids") List<Long> ids) {
        boolean delete = service.delete(ids);
        return delete ? BaseResp.ok(true) : BaseResp.error("删除失败");
    }

}
