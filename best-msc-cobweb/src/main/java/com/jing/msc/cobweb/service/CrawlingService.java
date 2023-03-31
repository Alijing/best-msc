package com.jing.msc.cobweb.service;

import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.NovelCrawlConfig;
import com.jing.msc.cobweb.entity.vo.CrawlConfig;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service
 * @date : 2021/11/12 10:19
 * @description :
 */
public interface CrawlingService {
    /**
     * 通过小说Id 获取爬取配置信息
     *
     * @param novelId 小说Id
     * @return 爬取配置信息
     * @author jing
     * @date 2023/3/31 10:32
     */
    NovelCrawlConfig crawlConfigByNovelId(Long novelId);

    /**
     * 新增或编辑小说信息
     *
     * @param config 配置信息
     * @return ID
     * @author jing
     * @date 2022/4/21 15:24
     */
    BaseResp<Boolean> saveOrUpdateConfig(CrawlConfig config);

    /**
     * 通过小说Id 复制爬取配置信息
     *
     * @param novelId 小说Id
     * @return j结果
     * @author jing
     * @date 2023/3/31 10:39
     */
    boolean configCopy(Long novelId);

    /**
     * 通过小说Id 爬取 章节信息
     *
     * @param novelId 小说Id
     * @return 结果
     * @author jing
     * @date 2021/11/12 10:28
     */
    BaseResp<Object> crawlingNovelChapter(Long novelId);

    /**
     * 爬取 小说 所有章节内容
     *
     * @param novelId 小说Id
     * @return 结果
     * @author jing
     * @date 2021/11/12 10:28
     */
    BaseResp<Object> crawlingNovelContent(Long novelId);

}
