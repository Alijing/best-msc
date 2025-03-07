package com.jing.msc.cobweb.service.crawl.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.base.BaseResp;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.SouthInterfaceUtils;
import com.jing.msc.cobweb.entity.book.Novel;
import com.jing.msc.cobweb.entity.crawl.CrawlConfig;
import com.jing.msc.cobweb.entity.crawl.dto.CrawlSchema;
import com.jing.msc.cobweb.mapper.crawl.CrawlConfigMapper;
import com.jing.msc.cobweb.service.NovelService;
import com.jing.msc.cobweb.service.crawl.CrawlConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "novelService")
    private NovelService novelService;

    @Value("${best-msc-py.url:''}")
    private String pythonServerUrl;

    @Override
    public List<CrawlConfig> crawlConfigByNovelId(Long novelId) {
        if (Objects.isNull(novelId)) {
            return Collections.emptyList();
        }
        QueryWrapper<CrawlConfig> configWrapper = new QueryWrapper<>();
        configWrapper.eq("novel_id", novelId);
        List<CrawlConfig> list = list(configWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public List<Long> saveBatchConfig(List<CrawlConfig> configs) {
        Assert.notEmpty(configs, "配置信息不能为空");
        remove(new QueryWrapper<CrawlConfig>().eq("novel_id", configs.get(0).getNovelId()));
        boolean saveBatch = saveBatch(configs);
        if (!saveBatch) {
            throw new CustomException(ResultEnum.SQL_EXCEPTION.getCode(), "配置信息保存失败");
        }
        return configs.stream().map(CrawlConfig::getId).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void configCopy(Long novelId) {
        Assert.notNull(novelId, "小说ID不能为空");
        Novel copy = new Novel();
        copy.setId(null);
        copy.setName("新复制的小说");
        copy.setPath("http://xxxxx");
        boolean save = novelService.save(copy);
        Assert.isTrue(save, "小说复制失败");
        List<CrawlConfig> configs = crawlConfigByNovelId(novelId);
        configs.forEach(it -> it.setNovelId(copy.getId()));

        saveBatchConfig(configs);
    }

    @Override
    public <T> T crawl(Long targetId, Type resultClass) {

        return null;
    }

    @Override
    public <T> T crawl(String api, CrawlSchema schema, Type resultClass) {
        Assert.notNull(schema, "爬取策略不能为空");
        Assert.notEmpty(pythonServerUrl, "Python服务地址不能为空");
        BaseResp<T> resp = SouthInterfaceUtils.sendHttpPostWithJson(pythonServerUrl + api, null, schema, resultClass);
        Assert.isTrue(resp.getCode() == 200, "爬取失败");
        return resp.getData();
    }

    @Override
    public <T> T crawl(Long targetId, Class<T> clazz) {
        return crawl(targetId, (Type) clazz);
    }


}
