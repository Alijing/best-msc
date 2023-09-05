package com.jing.msc.binbin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.msc.binbin.entity.BaseResp;
import com.jing.msc.binbin.entity.vo.DeliveryOrderMergeCfg;
import com.jing.msc.binbin.service.BinBinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.controller
 * @date : 2023/8/12 11:12
 * @description :
 */
@RestController
@RequestMapping("/binbin")
@Api(tags = "接口")
public class BinBinController {

    @Resource(name = "binBinService")
    private BinBinService service;

    @ApiOperation(value = "合并配送单信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "deliveryOrder/merge", produces = "application/json;charset=UTF-8")
    public void mergeDeliveryOrder(@RequestBody DeliveryOrderMergeCfg cfg, HttpServletRequest request, HttpServletResponse response) {
        service.mergeDeliveryOrder(cfg, request, response);
    }

    @ApiOperation(value = "合并配送单信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping(value = "test/merge")
    public BaseResp<String> mergeDeliveryOrder() {
        return BaseResp.ok("成功");
    }


}
