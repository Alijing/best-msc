package com.jing.msc.cobweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.dao.NovelMapper;
import com.jing.msc.cobweb.entity.Novel;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.entity.NovelContent;
import com.jing.msc.cobweb.entity.NovelCrawlConfig;
import com.jing.msc.cobweb.entity.vo.NovelVo;
import com.jing.msc.cobweb.service.NovelChapterService;
import com.jing.msc.cobweb.service.NovelContentService;
import com.jing.msc.cobweb.service.NovelCrawlConfigService;
import com.jing.msc.cobweb.service.NovelService;
import com.jing.msc.cobweb.util.NumberChangeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    @Autowired
    private NovelCrawlConfigService crawlConfigService;

    @Autowired
    private NovelChapterService chapterService;

    @Autowired
    private NovelContentService contentService;

    @Override
    public BaseResp<List<Novel>> novels(NovelVo novel) {
        QueryWrapper<Novel> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        if (null != novel.getName()) {
            wrapper.like("name", novel.getName());
        }
        IPage<Novel> page = new Page<>(novel.getCurrentPage(), novel.getPageSize());
        IPage<Novel> iPage = page(page, wrapper);
        return BaseResp.ok(iPage.getRecords()).setCurrentPage(iPage.getCurrent()).setTotal(iPage.getTotal()).setPageSize(iPage.getSize());
    }

    @Override
    public BaseResp<Boolean> batchDelete(NovelVo novel) {
        if (CollectionUtils.isEmpty(novel.getBatchId())) {
            return BaseResp.ok();
        }
        removeBatchByIds(novel.getBatchId());

        QueryWrapper<NovelCrawlConfig> configWrapper = new QueryWrapper<>();
        configWrapper.in("novel_id", novel.getBatchId());
        crawlConfigService.remove(configWrapper);

        QueryWrapper<NovelChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.select("id, novel_id");
        chapterWrapper.in("novel_id", novel.getBatchId());
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

}
