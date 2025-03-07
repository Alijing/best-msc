package com.jing.msc.cobweb.controller.book;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.crawl.CrawlConfig;
import com.jing.msc.cobweb.service.NovelService;
import com.jing.msc.cobweb.service.crawl.CrawlConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.util.List;

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

    @Resource(name = "crawlConfigService")
    private CrawlConfigService crawlService;

    @Resource(name = "novelService")
    private NovelService novelService;

    @Operation(summary = "通过 ID 查询小说爬取配置信息")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("config/{novelId}")
    public BaseResp<List<CrawlConfig>> crawlConfigByNovelId(@PathVariable("novelId") Long novelId) {
        return BaseResp.ok(crawlService.crawlConfigByNovelId(novelId));
    }

    @WebLog(description = "新增或编辑爬取小说的配置信息")
    @Operation(summary = "新增或编辑爬取小说的配置信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("config")
    public BaseResp<List<Long>> crawlConfUpdate(@RequestBody List<CrawlConfig> config) {
        return BaseResp.ok(crawlService.saveBatchConfig(config));
    }

    @Operation(summary = "通过 ID 复制小说爬取配置")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("config/copy/{novelId}")
    public BaseResp<Boolean> configCopy(@PathVariable("novelId") Long novelId) {
        crawlService.configCopy(novelId);
        return BaseResp.ok(true);
    }

    @Operation(summary = "爬取章节")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("chapter/{novelId}")
    public BaseResp<Integer> crawlingChapter(@PathVariable("novelId") Long novelId) throws MalformedURLException {
        return BaseResp.ok(novelService.crawlChapter(novelId));
    }

    @Operation(summary = "爬取章节内容")
    @ApiOperationSupport(author = "Jing", order = 3)
    @GetMapping("content/{novelId}")
    public BaseResp<Boolean> crawlingContent(@PathVariable("novelId") Long novelId) {
        novelService.crawlContent(novelId);
        return BaseResp.ok();
    }

}

