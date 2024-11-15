package com.jing.msc.cobweb.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.Novel;
import com.jing.msc.cobweb.entity.vo.NovelVo;
import com.jing.msc.cobweb.service.NovelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.controller
 * @date : 2021/11/11 16:48
 * @description :
 */
@RestController
@ApiSupport(order = 21)
@Tag(name = "小说相关接口", description = "小说相关接口描述")
@RequestMapping("novel")
public class NovelController {

    private final Logger logger = LoggerFactory.getLogger(NovelController.class);

    @Autowired
    private NovelService novelService;

    @WebLog(description = "查询所有小说信息")
    @Operation(summary = "查询所有小说信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    //@PreAuthorize("@sgex.hasAuthority('sys:novel:list1111')")
    @PostMapping("/list")
    public BaseResp<List<Novel>> novels(@RequestBody NovelVo info) {
        return novelService.novels(info);
    }

    @Operation(summary = "通过 ID 查询小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("info/{novelId}")
    public BaseResp<Novel> novelInfoById(@PathVariable("novelId") Long novelId) {
        return BaseResp.ok(novelService.getById(novelId));
    }

    @WebLog(description = "批量删除小说信息")
    @Operation(summary = "批量删除小说信息")
    @ApiOperationSupport(author = "Jing", order = 3)
    @PostMapping("batch/delete")
    public BaseResp<Boolean> batchDelete(@RequestBody NovelVo info) {
        return novelService.batchDelete(info);
    }

    @GetMapping("download/{novelId}")
    public void download(@PathVariable("novelId") Long novelId, HttpServletResponse response) {
        novelService.download(novelId, response);
    }

    @GetMapping("change/chapter/name/{novelId}")
    public BaseResp<Boolean> changeChapterName(@PathVariable("novelId") Long novelId) {
        return novelService.changeChapterName(novelId);
    }

}
