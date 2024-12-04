package com.jing.msc.cobweb.controller.book;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.book.Novel;
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
    @GetMapping("/list")
    public BaseResp<List<Novel>> novels(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "pageIndex") Integer current,
                                        @RequestParam(value = "pageSize") Integer size) {
        NovelVo novelVo = new NovelVo();
        if (null != name) {
            novelVo.setName(name);
        }
        novelVo.setPageIndex(current);
        novelVo.setPageSize(size);
        return novelService.novels(novelVo);
    }

    @Operation(summary = "通过 ID 查询小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("info/{novelId}")
    public BaseResp<Novel> novelInfoById(@PathVariable("novelId") Long novelId) {
        return BaseResp.ok(novelService.getById(novelId));
    }

    @Operation(summary = "新增小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("info")
    public BaseResp<Long> add(@RequestBody Novel novel) {
        boolean save = novelService.save(novel);
        return save ? BaseResp.ok(novel.getId()) : BaseResp.error("新增失败");
    }

    @Operation(summary = "编辑小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PutMapping("info")
    public BaseResp<Long> edit(@RequestBody Novel novel) {
        boolean save = novelService.updateById(novel);
        return save ? BaseResp.ok(novel.getId()) : BaseResp.error("编辑失败");
    }

    @WebLog(description = "批量删除小说信息")
    @Operation(summary = "批量删除小说信息")
    @ApiOperationSupport(author = "Jing", order = 3)
    @DeleteMapping("info")
    public BaseResp<Boolean> batchDelete(@RequestParam(value = "ids") List<Long> ids) {
        return novelService.batchDelete(ids);
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
