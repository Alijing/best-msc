package com.jing.msc.cobweb.processor;

import com.jing.msc.cobweb.entity.Novel;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.enums.MagicEnum;
import com.jing.msc.cobweb.service.NovelService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
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
public class NovelChapterProcessor implements MagicPageProcessor {

    private final Logger logger = LoggerFactory.getLogger(NovelChapterProcessor.class);

    private final static String[] NEXT_PAGE_NAME = new String[]{"下一页"};

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Autowired
    private NovelService service;

    @Override
    public void process(Page page, Object target) {
        if (null == target) {
            return;
        }
        Novel novel = service.getById(target.toString());
        if (null == novel || StringUtils.isBlank(novel.getChapterStyle())) {
            return;
        }

        List<Selectable> selectableList = page.getHtml().xpath(novel.getChapterStyle()).nodes();
        List<NovelChapter> chapters = new ArrayList<>();
        for (Selectable selectable : selectableList) {
            String name = selectable.xpath(novel.getChapterValueStyle()).toString();
            String path = selectable.links().get();
            if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(path)) {
                NovelChapter chapter = new NovelChapter(null, name, path, novel.getId(), novel.getContentStyle());
                chapters.add(chapter);
            }
        }
        if (StringUtils.isNotBlank(novel.getNextChapterStyle())) {
            List<String> targetRequests = new ArrayList<>();
            List<Selectable> nextNodes = page.getHtml().xpath(novel.getNextChapterStyle()).nodes();
            for (Selectable selectable : nextNodes) {
                String name = selectable.xpath(novel.getNextChapterValueStyle()).toString();
                boolean contains = Arrays.asList(NEXT_PAGE_NAME).contains(name);
                if (contains) {
                    String path = selectable.links().get();
                    targetRequests.add(path);
                    // 部分三：从页面发现后续的url地址来抓取
                    page.addTargetRequests(targetRequests);
                }
            }
        }
        page.putField(MagicEnum.NOVEL_CHAPTER.getKey(), chapters);
        //判断链接是否符合"http://m.lewen567.com/0/361/任意个数字"格式
//        if (page.getUrl().regex("http://m.lewen567.com/0/361/[0-9]+").match()) {
//            //定义如何抽取页面信息，并保存下来
//            List<Selectable> selectableList = page.getHtml().xpath("//section[@class='zjlb']/ul[@class='lb fk']").nodes();
//            for (Selectable selectable : selectableList) {
//                String name = selectable.xpath("//li/a/text()").toString();
//                logger.info("章节目录 : 【" + name + "】");
//            }
//        }
    }

    @Override
    public Site getSite() {
//        site.setCharset("GBK");
        site.setCharset("UTF-8");
        return site;
    }

}
