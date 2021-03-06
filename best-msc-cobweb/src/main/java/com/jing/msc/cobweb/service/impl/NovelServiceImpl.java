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
import com.jing.msc.cobweb.entity.vo.NovelVo;
import com.jing.msc.cobweb.service.NovelChapterService;
import com.jing.msc.cobweb.service.NovelContentService;
import com.jing.msc.cobweb.service.NovelService;
import com.jing.msc.cobweb.util.NumberChangeUtil;
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
    public BaseResp<Long> saveOrUpdateNovel(Novel novel) {
        try {
            boolean update = saveOrUpdate(novel);
            if (update) {
                return BaseResp.ok(novel.getId());
            }
            return BaseResp.error();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseResp.error(e.getMessage());
        }
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
                        .replaceAll("???", "")
                        .replaceAll("???", "")
                        .replaceAll("\\(", "")
                        .replaceAll("\\)", "");
                String cpr = "???" + NumberChangeUtil.digital2Chinese(i) + "??? ";
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
            logger.info("???????????????????????? : " + update);
            if (update) {
                return BaseResp.ok(true);
            }
            return BaseResp.error("?????????????????????????????????????????????");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseResp.error(e.getMessage());
        }
    }

}
