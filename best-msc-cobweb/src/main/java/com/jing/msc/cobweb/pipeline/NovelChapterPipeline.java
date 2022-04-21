package com.jing.msc.cobweb.pipeline;

import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.enums.MagicEnum;
import com.jing.msc.cobweb.service.NovelChapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.pipeline
 * @date : 2021/11/11 17:51
 * @description :
 */
@Component
public class NovelChapterPipeline implements Pipeline {

    private final Logger logger = LoggerFactory.getLogger(NovelChapterPipeline.class);

    @Autowired
    private NovelChapterService service;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<NovelChapter> chapters = resultItems.get(MagicEnum.NOVEL_CHAPTER.getKey());
        if (chapters != null && chapters.size() > 0) {
            boolean saveBatch = service.saveBatch(chapters);
            logger.info("小说章节保存结果 : " + saveBatch);
        }
    }

}
