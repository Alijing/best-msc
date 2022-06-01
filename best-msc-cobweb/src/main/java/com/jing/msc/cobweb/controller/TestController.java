package com.jing.msc.cobweb.controller;

import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.Spider;
import com.jing.msc.cobweb.service.impl.SpiderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "测试接口")
public class TestController {

    protected final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource(name = "spiderService")
    private SpiderServiceImpl service;

    @ApiOperation(value = "查询所有用户信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping(value = "/spider", produces = "application/json;charset=UTF-8")
    public BaseResp<List<Spider>> allSpider() {
        List<Spider> list = service.list();
        logger.error("-->>>>  : " + JSON.toJSONString(list));
        return BaseResp.ok(list);
    }

    @ApiOperation(value = "新增或者更新用户")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "/spider/update", produces = "application/json;charset=UTF-8")
    public BaseResp<List<Spider>> updateSpider(@Validated @RequestBody Spider info) {
        try {
            boolean update = service.saveOrUpdate(info);
            List<Spider> list = service.list();
            return BaseResp.ok(list);
        } catch (Exception e) {
            logger.error("-->>>>  : ", e);
            if (e instanceof DuplicateKeyException) {
                return BaseResp.error("账号已存在，请重新输入");
            }
        }
        return BaseResp.error();
    }

    @ApiOperation(value = "通过表名生成SQL")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping(value = "/spider/generator/{step}", produces = "application/json;charset=UTF-8")
    public BaseResp<List<Spider>> updateSpider(@PathVariable("step") Integer step) {
        try {
            service.generate(step);
            return BaseResp.ok();
        } catch (Exception e) {
            logger.error("-->>>>  : ", e);
            if (e instanceof DuplicateKeyException) {
                return BaseResp.error("账号已存在，请重新输入");
            }
        }
        return BaseResp.error();
    }

}
