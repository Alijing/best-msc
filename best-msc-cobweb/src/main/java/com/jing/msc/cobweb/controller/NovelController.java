package com.jing.msc.cobweb.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.Novel;
import com.jing.msc.cobweb.entity.vo.NovelVo;
import com.jing.msc.cobweb.service.CrawlingService;
import com.jing.msc.cobweb.service.NovelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "小说相关接口")
@RequestMapping("novel")
public class NovelController {

    private final Logger logger = LoggerFactory.getLogger(NovelController.class);

    @Autowired
    private CrawlingService crawlingService;

    @Autowired
    private NovelService novelService;

    @ApiOperation(value = "查询所有小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("/list")
    public BaseResp<List<Novel>> novels(@RequestBody NovelVo info) throws Exception {
        return novelService.novels(info);
    }

    @ApiOperation(value = "新增或编辑小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("/update")
    public BaseResp<Long> update(@RequestBody Novel novel) {
        return novelService.saveOrUpdateNovel(novel);
    }


    @ApiOperation(value = "通过 ID 查询小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("info/{novelId}")
    public BaseResp<Novel> novelInfoById(@PathVariable("novelId") Long novelId) {
        return BaseResp.ok(novelService.getById(novelId));
    }

    @ApiOperation(value = "通过 ID 复制小说爬取配置")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("info/copy/{novelId}")
    public BaseResp<Boolean> novelCopy(@PathVariable("novelId") Long novelId) {
        Novel byId = novelService.getById(novelId);
        byId.setId(null);
        byId.setName("新复制的小说");
        byId.setPath("http://xxxxx");
        return BaseResp.ok(novelService.save(byId));
    }


    @ApiOperation(value = "爬取章节")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("crawling/chapter/{novelId}")
    public BaseResp<Object> crawlingChapter(@PathVariable("novelId") Long novelId) {
        return crawlingService.crawlingNovelChapter(novelId);
    }

    @GetMapping("crawling/content/{novelId}")
    public BaseResp<Object> crawlingContent(@PathVariable("novelId") Long novelId) {
        return crawlingService.crawlingNovelContent(novelId);
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
