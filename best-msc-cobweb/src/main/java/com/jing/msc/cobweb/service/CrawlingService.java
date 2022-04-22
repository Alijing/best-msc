package com.jing.msc.cobweb.service;

import com.jing.common.core.base.BaseResp;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service
 * @date : 2021/11/12 10:19
 * @description :
 */
public interface CrawlingService {

    /**
     * 通过小说Id 爬取 章节信息
     *
     * @param novelId 小说Id
     * @author jing
     * @date 2021/11/12 10:28
     */
    BaseResp<Object> crawlingNovelChapter(Long novelId);

    /**
     * 爬取 小说 所有章节内容
     *
     * @param novelId 小说Id
     * @author jing
     * @date 2021/11/12 10:28
     */
    BaseResp<Object> crawlingNovelContent(Long novelId);

}
