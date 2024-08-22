package com.jing.msc.binbin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.msc.binbin.entity.vo.DeliveryOrderMergeCfg;
import com.jing.msc.binbin.service.BinBinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "BinBin接口")
public class BinBinController {

    @Resource(name = "binBinService")
    private BinBinService service;

    @Operation(summary = "合并配送单信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "deliveryOrder/merge", produces = "application/json;charset=UTF-8")
    public void mergeDeliveryOrder(@RequestBody DeliveryOrderMergeCfg cfg, HttpServletRequest request, HttpServletResponse response) {
        service.mergeDeliveryOrder(cfg, request, response);
    }

    @Operation(summary = "货物金额求和")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping(value = "goods/sum", produces = "application/json;charset=UTF-8")
    public void goodsSum(@RequestBody DeliveryOrderMergeCfg cfg, HttpServletRequest request, HttpServletResponse response) {
        service.goodsSum(cfg, request, response);
    }


}
