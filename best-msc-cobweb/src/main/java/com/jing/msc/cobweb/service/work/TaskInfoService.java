package com.jing.msc.cobweb.service.work;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 禅道上的日报处理服务类
 *
 * @author : jing
 * @since : 2024/7/16 14:14
 */
public interface TaskInfoService {


    /**
     * 获取任务信息
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param request   请求
     * @param response  响应
     */
    void taskList(String startDate, String endDate, HttpServletRequest request, HttpServletResponse response);


    /**
     * 获取任务信息
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param request   请求
     * @param response  响应
     */
    void taskSplit(String startDate, String endDate, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取加班信息
     *
     * @param startDate
     * @param endDate
     * @param request
     * @param response
     */
    void obtainOvertimeInfo(String startDate, String endDate, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException;

}
