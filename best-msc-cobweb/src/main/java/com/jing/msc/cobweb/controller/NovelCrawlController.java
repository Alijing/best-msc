package com.jing.msc.cobweb.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.NovelCrawlConfig;
import com.jing.msc.cobweb.entity.vo.CrawlConfig;
import com.jing.msc.cobweb.service.CrawlingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author : jing
 * @packageName : com.jing.msc.cobweb.controller
 * @description :
 * @since : 2023-03-31 09:41:11
 */
@RestController
@ApiSupport(order = 22)
@Tag(name = "爬取小说相关接口", description = "爬取小说相关接口描述")
@RequestMapping("/novel/crawl")
public class NovelCrawlController {

    @Autowired
    private CrawlingService crawlingService;

    @Operation(summary = "通过 ID 查询小说爬取配置信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("config/{novelId}")
    public BaseResp<NovelCrawlConfig> crawlConfigByNovelId(@PathVariable("novelId") Long novelId) {
        return BaseResp.ok(crawlingService.crawlConfigByNovelId(novelId));
    }

    @WebLog(description = "新增或编辑爬取小说的配置信息")
    @Operation(summary = "新增或编辑爬取小说的配置信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("config/update")
    public BaseResp<Boolean> configUpdate(@RequestBody CrawlConfig config) {
        return crawlingService.saveOrUpdateConfig(config);
    }

    @Operation(summary = "通过 ID 复制小说爬取配置")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("config/copy/{novelId}")
    public BaseResp<Boolean> configCopy(@PathVariable("novelId") Long novelId) {
        if (!crawlingService.configCopy(novelId)) {
            return BaseResp.error("配置信息复制失败");
        }
        return BaseResp.ok();
    }

    @Operation(summary = "爬取章节")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("chapter/{novelId}")
    public BaseResp<Object> crawlingChapter(@PathVariable("novelId") Long novelId) {
        return crawlingService.crawlingNovelChapter(novelId);
    }

    @GetMapping("content/{novelId}")
    public BaseResp<Object> crawlingContent(@PathVariable("novelId") Long novelId) {
        return crawlingService.crawlingNovelContent(novelId);
    }

}

