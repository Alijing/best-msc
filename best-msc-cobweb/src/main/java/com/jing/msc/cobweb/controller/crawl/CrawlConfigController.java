package com.jing.msc.cobweb.controller.crawl;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.entity.crawl.CrawlConfig;
import com.jing.msc.cobweb.service.crawl.CrawlConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : jing
 * @since : 2024/12/18 15:07
 */
@RestController
@ApiSupport(order = 22)
@Tag(name = "爬取配置相关接口", description = "爬取配置相关接口描述")
@RequestMapping("/crawl")
public class CrawlConfigController {

    @Resource(name = "crawlConfigService")
    private CrawlConfigService crawlService;

    @WebLog(description = "配置信息新增")
    @Operation(summary = "配置信息新增")
    @ApiOperationSupport(author = "Jing", order = 1)
    @PostMapping("config")
    public BaseResp<Boolean> saveBatchConfig(@RequestBody List<CrawlConfig> configs) {
        return crawlService.saveBatchConfig(configs);
    }


}
