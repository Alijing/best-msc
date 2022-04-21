package com.jing.msc.cobweb.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.entity.NovelContent;
import com.jing.msc.cobweb.enums.MagicEnum;
import com.jing.msc.cobweb.service.NovelChapterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

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

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Autowired
    private NovelChapterService service;

    @Override
    public void process(Page page) {
        QueryWrapper<NovelChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path", page.getUrl().toString());
        NovelChapter chapter = service.getOne(queryWrapper);
        if (null == chapter || StringUtils.isBlank(chapter.getContentStyle())) {
            return;
        }
        Selectable xpath = page.getHtml().xpath(chapter.getContentStyle());
        String content = xpath.toString()
                .replaceAll("<br>", "\n")
                .replaceAll("&nbsp;", "")
                .replaceAll("</dd>", "");
        NovelContent novelContent = new NovelContent(chapter.getId(), content);
        page.putField(MagicEnum.NOVEL_CONTENT.getKey(), novelContent);
    }

    @Override
    public Site getSite() {
        site.setCharset("GBK");
        return site;
    }
}
