package com.jing.msc.cobweb.service.crawl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.crawl.CrawlConfig;

import java.util.List;

/**
 * <p>
 * 爬取配置 服务类
 * </p>
 *
 * @author : jing
 * @since : 2024-12-18 11:35:10
 */
public interface CrawlConfigService extends IService<CrawlConfig> {

    /**
     * 批量保存配置
     *
     * @param configs 配置信息
     * @return 保存结果
     */
    BaseResp<Boolean> saveBatchConfig(List<CrawlConfig> configs);

}
