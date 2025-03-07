package com.jing.msc.cobweb.service.crawl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.crawl.CrawlConfig;
import com.jing.msc.cobweb.entity.crawl.dto.CrawlSchema;

import java.lang.reflect.Type;
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
     * 通过小说Id 获取爬取配置信息
     *
     * @param novelId 小说Id
     * @return 爬取配置信息
     * @author jing
     * @date 2023/3/31 10:32
     */
    List<CrawlConfig> crawlConfigByNovelId(Long novelId);

    /**
     * 批量保存配置
     *
     * @param configs 配置信息
     * @return 保存结果
     */
    List<Long> saveBatchConfig(List<CrawlConfig> configs);

    /**
     * 通过小说Id 复制爬取配置信息
     *
     * @param novelId 小说Id
     * @return j结果
     * @author jing
     * @date 2023/3/31 10:39
     */
    void configCopy(Long novelId);

    /**
     * 通过 目标Id 爬取数据
     *
     * @param targetId 目标Id
     * @return 结果
     * @author jing
     * @date 2025/03/07 10:39
     */
    <T> T crawl(Long targetId, Class<T> clazz);

    /**
     * 通过 目标Id 爬取数据
     *
     * @param targetId 目标Id
     * @return 结果
     * @author jing
     * @date 2025/03/07 10:39
     */
    <T> T crawl(Long targetId, Type resultClass);


    /**
     * 爬取数据
     *
     * @param api         接口
     * @param schema      爬取策略
     * @param resultClass 结果类型
     * @return 结果
     */
    <T> T crawl(String api, CrawlSchema schema, Type resultClass);

}
