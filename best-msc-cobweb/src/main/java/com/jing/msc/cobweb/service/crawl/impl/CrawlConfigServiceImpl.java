package com.jing.msc.cobweb.service.crawl.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.crawl.CrawlConfig;
import com.jing.msc.cobweb.mapper.crawl.CrawlConfigMapper;
import com.jing.msc.cobweb.service.crawl.CrawlConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 爬取配置 服务实现类
 * </p>
 *
 * @author : jing
 * @since : 2024-12-18 11:35:10
 */
@Service(value = "crawlConfigService")
public class CrawlConfigServiceImpl extends ServiceImpl<CrawlConfigMapper, CrawlConfig> implements CrawlConfigService {

    @Override
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public BaseResp<Boolean> saveBatchConfig(List<CrawlConfig> configs) {
        Assert.notEmpty(configs, "配置信息不能为空");
        boolean saveBatch = saveBatch(configs);
        return saveBatch ? BaseResp.ok() : BaseResp.error("配置信息保存失败");
    }

}
