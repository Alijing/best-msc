package com.jing.msc.cobweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.mapper.NovelCrawlConfigMapper;
import com.jing.msc.cobweb.entity.NovelCrawlConfig;
import com.jing.msc.cobweb.service.NovelCrawlConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 * @author : jing
 * @packageName : com.jing.msc.cobweb.service.impl
 * @since : 2023-03-31 09:41:11
 * @description :
 */
@Service(value = "NovelCrawlConfigService")
public class NovelCrawlConfigServiceImpl extends ServiceImpl<NovelCrawlConfigMapper, NovelCrawlConfig> implements NovelCrawlConfigService {

}
