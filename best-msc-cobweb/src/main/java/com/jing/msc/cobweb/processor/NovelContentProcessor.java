package com.jing.msc.cobweb.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.entity.NovelContent;
import com.jing.msc.cobweb.entity.NovelCrawlConfig;
import com.jing.msc.cobweb.enums.MagicEnum;
import com.jing.msc.cobweb.service.NovelChapterService;
import com.jing.msc.cobweb.service.NovelCrawlConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.processor
 * @date : 2021/11/11 17:02
 * @description :
 */
@Component
public class NovelContentProcessor implements PageProcessor {

    private final Logger logger = LoggerFactory.getLogger(NovelContentProcessor.class);

    private final static String[] NEXT_PAGE_NAME = new String[]{"下一页"};

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Autowired
    private NovelChapterService service;

    @Autowired
    private NovelCrawlConfigService crawlConfigService;

    @Override
    public void process(Page page) {
        QueryWrapper<NovelChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path", page.getUrl().toString());
        NovelChapter chapter = service.getOne(queryWrapper);
        if (null == chapter || StringUtils.isBlank(chapter.getContentStyle())) {
            return;
        }

        QueryWrapper<NovelCrawlConfig> configWrapper = new QueryWrapper<>();
        configWrapper.eq("novel_id", chapter.getNovelId());
        NovelCrawlConfig config = crawlConfigService.getOne(configWrapper);

        Selectable xpath = page.getHtml().xpath(chapter.getContentStyle());
        String content = xpath.toString()
                .replaceAll("<br>", "\n")
                .replaceAll("&nbsp;", "")
                .replaceAll("</dd>", "");
        NovelContent novelContent = new NovelContent(chapter.getId(), content);

        if (StringUtils.isNotBlank(config.getNextContentStyle())) {
            nextContent(config, page);
        }

        page.putField(MagicEnum.NOVEL_CONTENT.getKey(), novelContent);
    }

    private void nextContent(NovelCrawlConfig config, Page page) {
        List<String> targetRequests = new ArrayList<>();
        List<Selectable> nextNodes = page.getHtml().xpath(config.getNextContentStyle()).nodes();
        for (Selectable selectable : nextNodes) {
            String name = selectable.xpath(config.getNextContentValueStyle()).toString();
            boolean contains = Arrays.asList(NEXT_PAGE_NAME).contains(name);
            if (contains) {
                String path = selectable.links().get();
                targetRequests.add(path);
                // 部分三：从页面发现后续的url地址来抓取
                page.addTargetRequests(targetRequests);
            }
        }
    }

    @Override
    public Site getSite() {
//        site.setCharset("GBK");
        site.setCharset("UTF-8");
        return site;
    }

}
