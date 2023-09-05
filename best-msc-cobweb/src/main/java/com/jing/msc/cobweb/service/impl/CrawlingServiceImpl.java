package com.jing.msc.cobweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jing.common.core.base.BaseResp;
import com.jing.common.core.util.JSONUtil;
import com.jing.msc.cobweb.config.HttpClientDownloader;
import com.jing.msc.cobweb.entity.Novel;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.entity.NovelCrawlConfig;
import com.jing.msc.cobweb.entity.vo.CrawlConfig;
import com.jing.msc.cobweb.pipeline.NovelChapterPipeline;
import com.jing.msc.cobweb.pipeline.NovelContentPipeline;
import com.jing.msc.cobweb.processor.NovelChapterProcessor;
import com.jing.msc.cobweb.processor.NovelContentProcessor;
import com.jing.msc.cobweb.service.CrawlingService;
import com.jing.msc.cobweb.service.NovelChapterService;
import com.jing.msc.cobweb.service.NovelCrawlConfigService;
import com.jing.msc.cobweb.service.NovelService;
import com.jing.msc.cobweb.spider.MagicSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service.impl
 * @date : 2021/11/12 10:19
 * @description :
 */
@Service("crawlingService")
public class CrawlingServiceImpl implements CrawlingService {

    private final Logger logger = LoggerFactory.getLogger(NovelServiceImpl.class);

    @Autowired
    private NovelChapterProcessor chapterProcessor;

    @Autowired
    private NovelContentProcessor contentProcessor;

    @Autowired
    private NovelChapterPipeline chapterPipeline;
    @Autowired
    private NovelContentPipeline contentPipeline;

    @Autowired
    private NovelService novelService;

    @Autowired
    private NovelChapterService chapterService;

    @Autowired
    private NovelCrawlConfigService crawlConfigService;

    @Override
    public NovelCrawlConfig crawlConfigByNovelId(Long novelId) {
        if (novelId == null) {
            return null;
        }
        QueryWrapper<NovelCrawlConfig> configWrapper = new QueryWrapper<>();
        configWrapper.eq("novel_id", novelId);
        return crawlConfigService.getOne(configWrapper);
    }

    @Override
    public BaseResp<Boolean> saveOrUpdateConfig(CrawlConfig config) {
        try {
            String json = JSONUtil.toJson(config);
            Novel novel = JSONUtil.toBean(json, Novel.class);
            novel.setId(config.getNovelId());
            boolean update = novelService.saveOrUpdate(novel);
            if (!update) {
                return BaseResp.error();
            }
            NovelCrawlConfig crawlConfig = JSONUtil.toBean(json, NovelCrawlConfig.class);
            update = crawlConfigService.saveOrUpdate(crawlConfig);
            return BaseResp.ok(update);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseResp.error(e.getMessage());
        }
    }

    @Override
    public boolean configCopy(Long novelId) {
        Novel byId = novelService.getById(novelId);
        byId.setId(null);
        byId.setName("新复制的小说");
        byId.setPath("http://xxxxx");
        boolean save = novelService.save(byId);
        if (!save) {
            return false;
        }
        NovelCrawlConfig config = crawlConfigByNovelId(novelId);
        config.setNovelId(byId.getId());
        config.setStatus(0);
        return crawlConfigService.save(config);
    }

    @Override
    public BaseResp<Object> crawlingNovelChapter(Long novelId) {
        try {
            Novel novel = novelService.getById(novelId);
            if (null == novel) {
                return BaseResp.error("该小说不存在");
            }
            MagicSpider.create(chapterProcessor)
                    //从https://qd.anjuke.com/community/开始爬取
                    .addUrl(novel.getPath())
                    .setTarget(novelId)
                    // 使用自定义的Pipeline
                    .addPipeline(chapterPipeline)
                    .setDownloader(new HttpClientDownloader())
                    .thread(1)
                    .run();
            return BaseResp.ok();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseResp.error(e.getMessage());
        }
    }

    @Override
    public BaseResp<Object> crawlingNovelContent(Long novelId) {
        try {
            QueryWrapper<NovelChapter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("novel_id", novelId);
            queryWrapper.eq("status", 0);
            List<NovelChapter> chapters = chapterService.list(queryWrapper);
            if (null == chapters) {
                return BaseResp.error("无可更新内容的章节，不用爬啦");
            }
//            Spider.create(contentProcessor)
//                    //从https://qd.anjuke.com/community/开始爬取
//                    .addUrl(chapters.get(0).getPath())
//                    // 使用自定义的Pipeline
//                    .addPipeline(contentPipeline)
//                    .setDownloader(new HttpClientDownloader())
//                    .thread(1)
//                    .run();
            for (NovelChapter ch : chapters) {
                Spider.create(contentProcessor)
                        //从https://qd.anjuke.com/community/开始爬取
                        .addUrl(ch.getPath())
                        // 使用自定义的Pipeline
                        .addPipeline(contentPipeline)
                        .setDownloader(new HttpClientDownloader())
                        .thread(1)
                        .run();
            }
            return BaseResp.ok();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseResp.error(e.getMessage());
        }
    }

}
