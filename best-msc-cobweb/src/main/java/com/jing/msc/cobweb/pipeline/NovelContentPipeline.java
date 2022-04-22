package com.jing.msc.cobweb.pipeline;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.entity.NovelContent;
import com.jing.msc.cobweb.enums.MagicEnum;
import com.jing.msc.cobweb.service.NovelChapterService;
import com.jing.msc.cobweb.service.NovelContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.pipeline
 * @date : 2021/11/11 17:51
 * @description :
 */
@Component
public class NovelContentPipeline implements Pipeline {

    private final Logger logger = LoggerFactory.getLogger(NovelContentPipeline.class);

    @Autowired
    private NovelContentService service;

    @Autowired
    private NovelChapterService chapterService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        NovelContent content = resultItems.get(MagicEnum.NOVEL_CONTENT.getKey());
        if (null == content) {
            return;
        }
        boolean save = service.saveOrUpdate(content);
        logger.info("小说章节内容保存结果 : " + save);
        if (!save) {
            return;
        }
        UpdateWrapper<NovelChapter> updateWrapper = new UpdateWrapper<>();
        NovelChapter chapter = new NovelChapter();
        chapter.setStatus(1);
        updateWrapper.eq("id", content.getChapterId());
        boolean update = chapterService.update(chapter, updateWrapper);
        logger.info("小说章节状态更新结果 : " + update);

    }

}
