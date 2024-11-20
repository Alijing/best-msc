package com.jing.msc.cobweb.controller;

import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.service.sys.impl.SpiderServiceImpl;
import com.jing.msc.security.entity.Spider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.controller
 * @date : 2021/4/23 14:42
 * @description :
 */
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口")
public class TestController {

    protected final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource(name = "spiderService")
    private SpiderServiceImpl service;

    @Operation(summary = "查询所有用户信息")
    @PreAuthorize("hasAuthority('test11')")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping(value = "/spider", produces = "application/json;charset=UTF-8")
    public BaseResp<List<Spider>> allSpider() {
        List<Spider> list = service.list();
        logger.error("-->>>>  : " + JSON.toJSONString(list));
        return BaseResp.ok(list);
    }

    @Operation(summary = "新增或者更新用户")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "/spider/update", produces = "application/json;charset=UTF-8")
    public BaseResp<List<Spider>> updateSpider(@Validated @RequestBody Spider info) {
        boolean update = service.saveOrUpdate(info);
        List<Spider> list = service.list();
        return BaseResp.ok(list);
    }

    @Operation(summary = "通过 excel 导入")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "/read/excel")
    public BaseResp<Boolean> readExcel(Integer type, MultipartFile file) {
        service.readResGroupExcel(type, file);
        return BaseResp.ok();
    }

    @Operation(summary = "生成 sql ")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping(value = "/generator/sql/{targetNodeId}/{startId}/{fieldNum}")
    public BaseResp<Boolean> generateSql(
            @PathVariable("targetNodeId") Integer targetNodeId,
            @PathVariable("startId") Integer startId,
            @PathVariable("fieldNum") Integer fieldNum) {
        service.generateSql(startId, targetNodeId, fieldNum);
        return BaseResp.ok();
    }

}
