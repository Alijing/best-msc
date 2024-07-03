package com.jing.msc.binbin.service;

import com.jing.msc.binbin.entity.vo.DeliveryOrderMergeCfg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.binbin.service
 * @date : 2023/8/12 11:15
 * @description :
 */
public interface BinBinService {


    /**
     * 合并配送单信息
     *
     * @param cfg      配送单文件夹路径
     * @param request  request
     * @param response response
     * @author jing
     * @date 2023/8/12 11:18
     */
    void mergeDeliveryOrder(DeliveryOrderMergeCfg cfg, HttpServletRequest request, HttpServletResponse response);

    /**
     * 合并配送单信息
     *
     * @param cfg      配送单文件夹路径
     * @param request  request
     * @param response response
     * @author jing
     * @date 2023/8/12 11:18
     */
    void goodsSum(DeliveryOrderMergeCfg cfg, HttpServletRequest request, HttpServletResponse response);


}
