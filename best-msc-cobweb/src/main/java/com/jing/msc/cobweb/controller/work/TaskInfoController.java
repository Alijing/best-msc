package com.jing.msc.cobweb.controller.work;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.service.work.TaskInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : jing
 * @since : 2024/7/16 14:33
 */
@Tag(name = "工作相关接口")
@RestController
@RequestMapping("/work")
public class TaskInfoController {

    @Resource(name = "taskInfoService")
    private TaskInfoService service;

    @WebLog(description = "获取任务信息")
    @Operation(summary = "获取任务信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/task")
    public void list(@RequestParam(value = "startDate") String startDate,
                     @RequestParam(value = "endDate") String endDate,
                     HttpServletRequest request, HttpServletResponse response) {
        service.taskList(startDate, endDate, request, response);
    }

    @WebLog(description = "获取任务信息")
    @Operation(summary = "获取任务信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/task/split")
    public void taskSplit(@RequestParam(value = "startDate") String startDate,
                          @RequestParam(value = "endDate") String endDate,
                          HttpServletRequest request, HttpServletResponse response) {
        service.taskSplit(startDate, endDate, request, response);
    }


}
