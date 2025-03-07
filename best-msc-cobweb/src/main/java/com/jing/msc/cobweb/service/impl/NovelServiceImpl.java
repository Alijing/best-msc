package com.jing.msc.cobweb.service.impl;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.base.BasePageResp;
import com.jing.common.core.base.BaseResp;
import com.jing.common.core.util.StringUtils;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.entity.NovelContent;
import com.jing.msc.cobweb.entity.NovelCrawlConfig;
import com.jing.msc.cobweb.entity.book.Novel;
import com.jing.msc.cobweb.entity.crawl.CrawlConfig;
import com.jing.msc.cobweb.entity.crawl.dto.CrawlSchema;
import com.jing.msc.cobweb.entity.vo.NovelVo;
import com.jing.msc.cobweb.enums.crawl.DataType;
import com.jing.msc.cobweb.mapper.NovelMapper;
import com.jing.msc.cobweb.service.NovelChapterService;
import com.jing.msc.cobweb.service.NovelContentService;
import com.jing.msc.cobweb.service.NovelCrawlConfigService;
import com.jing.msc.cobweb.service.NovelService;
import com.jing.msc.cobweb.service.crawl.CrawlConfigService;
import com.jing.msc.cobweb.util.NumberChangeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service.impl
 * @date : 2021/11/11 16:50
 * @description :
 */
@Service("novelService")
@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

    private final Logger logger = LoggerFactory.getLogger(NovelServiceImpl.class);

    @Resource(name = "crawlConfigService")
    private CrawlConfigService crawlService;

    @Autowired
    private NovelCrawlConfigService crawlConfigService;

    @Autowired
    private NovelChapterService chapterService;

    @Autowired
    private NovelContentService contentService;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public BaseResp<List<Novel>> novels(NovelVo novel) {
        QueryWrapper<Novel> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        if (null != novel.getName()) {
            wrapper.like("name", novel.getName());
        }
        IPage<Novel> page = new Page<>(novel.getPageIndex(), novel.getPageSize());
        IPage<Novel> iPage = page(page, wrapper);
        return BasePageResp.ok(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override
    public BaseResp<Boolean> batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return BaseResp.ok();
        }
        removeBatchByIds(ids);

        QueryWrapper<NovelCrawlConfig> configWrapper = new QueryWrapper<>();
        configWrapper.in("novel_id", ids);
        crawlConfigService.remove(configWrapper);

        QueryWrapper<NovelChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.select("id, novel_id");
        chapterWrapper.in("novel_id", ids);
        List<NovelChapter> chapters = chapterService.list(chapterWrapper);
        chapterService.remove(chapterWrapper);

        if (CollectionUtils.isEmpty(chapters)) {
            return BaseResp.ok();
        }

        List<Long> chapterId = chapters.stream().map(NovelChapter::getId).collect(Collectors.toList());
        QueryWrapper<NovelContent> contentWrapper = new QueryWrapper<>();
        contentWrapper.in("chapter_id", chapterId);
        contentService.remove(contentWrapper);
        return BaseResp.ok();
    }

    @Override
    public void download(Long novelId, HttpServletResponse response) {
        try {
            Novel novel = getById(novelId);
            if (null == novel) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            QueryWrapper<NovelChapter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("novel_id", novelId);
            List<NovelChapter> chapters = chapterService.list(queryWrapper);
            for (NovelChapter ch : chapters) {
                builder.append(ch.getName()).append("\n");
                QueryWrapper<NovelContent> contentQueryWrapper = new QueryWrapper<>();
                contentQueryWrapper.eq("chapter_id", ch.getId());
                NovelContent one = contentService.getOne(contentQueryWrapper);
                builder.append(one.getContent()).append("\n\n");
            }
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(novel.getName() + ".txt", "UTF-8"));
            response.getOutputStream().write(builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public BaseResp<Boolean> changeChapterName(Long novelId) {
        try {
            QueryWrapper<NovelChapter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("novel_id", novelId);
            List<NovelChapter> chapters = chapterService.list(queryWrapper);
            List<NovelChapter> candidate = new ArrayList<>();
            for (int i = 1; i <= chapters.size(); i++) {
                NovelChapter chapter = chapters.get(i - 1);
                String name = chapter.getName().replaceAll("\\[", "")
                        .replaceAll("]", "")
                        .replaceAll("【", "")
                        .replaceAll("】", "")
                        .replaceAll("\\(", "")
                        .replaceAll("\\)", "");
                String cpr = "第" + NumberChangeUtil.digital2Chinese(i) + "章 ";
                if (name.contains(cpr)) {
                    continue;
                }
                chapter.setName(cpr + name);
                candidate.add(chapter);
            }
            if (candidate.size() < 1) {
                return BaseResp.ok(true);
            }
            boolean update = chapterService.updateBatchById(candidate);
            logger.info("修改章节名称结果 : " + update);
            if (update) {
                return BaseResp.ok(true);
            }
            return BaseResp.error("章节名称修改失败，请联系管理员");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseResp.error(e.getMessage());
        }
    }

    @Override
    public Integer crawlChapter(Long novelId) throws MalformedURLException {
        Assert.notNull(novelId, "小说ID不能为空");
        Novel novel = getById(novelId);
        Assert.notNull(novel, "小说不存在");
        Assert.notNull(novel.getPath(), "小说路径不能为空");

        ParameterizedType inner = new ParameterizedTypeImpl(new Type[]{NovelChapter.class}, null, List.class);
        ParameterizedType outer = new ParameterizedTypeImpl(new Type[]{inner}, null, BaseResp.class);
        List<NovelChapter> chapters = crawlService.crawl("/api/v1/crawl", getCrawlSchema(novelId, novel.getPath(), DataType.NOVEL_CHAPTER), outer);
        if (CollectionUtils.isEmpty(chapters)) {
            return 0;
        }

        URL url = new URL(novel.getPath());

        QueryWrapper<NovelChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("novel_id", novelId);
        chapterService.remove(chapterWrapper);

        Iterator<NovelChapter> iterator = chapters.iterator();
        while (iterator.hasNext()) {
            NovelChapter next = iterator.next();
            if (!next.getPath().contains("/")) {
                iterator.remove();
                continue;
            }

            if (!(next.getPath().startsWith("http") || next.getPath().startsWith("https"))) {
                next.setPath(url.getProtocol() + "://" + url.getHost() + next.getPath());
            }
            next.setNovelId(novelId);
            next.setStatus(0);
        }

        chapterService.saveBatch(chapters);
        return chapters.size();
    }

    @Override
    public void crawlContent(Long novelId) {
        Assert.notNull(novelId, "小说ID不能为空");

        QueryWrapper<NovelChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("novel_id", novelId);
        chapterWrapper.eq("status", 0);
        List<NovelChapter> chapters = chapterService.list(chapterWrapper);
        Assert.notEmpty(chapters, "没有可拉取的章节");

        List<NovelContent> contents = new ArrayList<>();
        List<Long> crawled = new ArrayList<>();
        ParameterizedType outer = new ParameterizedTypeImpl(new Type[]{String.class}, null, BaseResp.class);
        CrawlSchema crawlSchema = getCrawlSchema(novelId, null, DataType.NOVEL_CONTENT);
        taskExecutor.execute(() -> {
            int success = 0;
            for (NovelChapter it : chapters) {
                crawlSchema.setUrl(it.getPath());
                String content = crawlService.crawl("/api/v1/crawl/basic", crawlSchema, outer);
                if (StringUtils.isBlank(content)) {
                    continue;
                }
                success += 1;
                logger.info("拉取成功章节 : {}", it.getName());

                NovelContent nc = new NovelContent();
                nc.setChapterId(it.getId());
                nc.setContent(content);
                contents.add(nc);

                crawled.add(it.getId());

                if (contents.size() < 10) {
                    continue;
                }

                contentService.saveBatch(contents);

                UpdateWrapper<NovelChapter> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("status", 1);
                updateWrapper.in("id", crawled);
                chapterService.update(updateWrapper);

                contents.clear();
                crawled.clear();
            }

            contentService.saveBatch(contents);

            UpdateWrapper<NovelChapter> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status", 1);
            updateWrapper.in("id", crawled);
            chapterService.update(updateWrapper);

            logger.info("章节拉取完成 : {}", success);
        });
    }


    private CrawlSchema getCrawlSchema(Long novelId, String path, DataType type) {
        Assert.notNull(novelId, "目标小说Id不能为空");
        Assert.notNull(path, "目标地址 url 不能为空");

        QueryWrapper<CrawlConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("novel_id", novelId);
        wrapper.eq("type", type.getValue());
        CrawlConfig config = crawlService.getOne(wrapper);
        Assert.notNull(config, "配置信息为空");

        CrawlSchema schema = new CrawlSchema();
        schema.setUrl(path);
        schema.setMode(config.getMode().getValue());
        schema.setName(config.getName());
        schema.setBaseSelector(config.getBaseSelector());
        schema.setFields(config.getFields());

        return schema;
    }

}
