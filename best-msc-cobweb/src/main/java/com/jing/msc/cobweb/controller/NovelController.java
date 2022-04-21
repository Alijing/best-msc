package com.jing.msc.cobweb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.Novel;
import com.jing.msc.cobweb.service.CrawlingService;
import com.jing.msc.cobweb.service.NovelService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.controller
 * @date : 2021/11/11 16:48
 * @description :
 */
@RestController
@RequestMapping("novel")
public class NovelController {

    private final Logger logger = LoggerFactory.getLogger(NovelController.class);

    @Autowired
    private CrawlingService crawlingService;

    @Autowired
    private NovelService novelService;

    @ApiOperation(value = "查询所有小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("/list")
    public BaseResp<List<Novel>> novels(@RequestBody Novel info) {
        try {
            if (null != info.getName()) {
                QueryWrapper<Novel> wrapper = new QueryWrapper<>();
                wrapper.like("name", info.getName());
                return BaseResp.ok(novelService.list(wrapper));
            }
            return BaseResp.ok(novelService.list());
        } catch (Exception e) {
            logger.error("", e);
            return BaseResp.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "新增或编辑小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("/update")
    public BaseResp<Long> update(@RequestBody Novel novel) {
        try {
            logger.info(novel.toString());
            boolean update = novelService.saveOrUpdate(novel);
            if (update) {
                return BaseResp.ok(novel.getId());
            }
            return BaseResp.fail("失败");
        } catch (Exception e) {
            logger.error("", e);
            return BaseResp.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "通过 ID 查询小说信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("info/{novelId}")
    public BaseResp<Novel> novelInfoById(@PathVariable("novelId") Long novelId) {
        try {
            return BaseResp.ok(novelService.getById(novelId));
        } catch (Exception e) {
            logger.error("", e);
            return BaseResp.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "查询所有用户信息")
    @ApiOperationSupport(author = "Jing", order = 2)
    @GetMapping("crawling/chapter/{novelId}")
    public void crawlingChapter(@PathVariable("novelId") Long novelId) {
        try {
            crawlingService.crawlingNovelChapter(novelId);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @GetMapping("crawling/content/{novelId}")
    public void crawlingContent(@PathVariable("novelId") Long novelId) {
        try {
            crawlingService.crawlingNovelContent(novelId);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @GetMapping("download/{novelId}")
    public void download(@PathVariable("novelId") Long novelId, HttpServletResponse response) {
        try {
            novelService.download(novelId, response);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @GetMapping("change/chapter/name/{novelId}")
    public void changeChapterName(@PathVariable("novelId") Long novelId, HttpServletResponse response) {
        try {
            novelService.changeChapterName(novelId, response);
        } catch (Exception e) {
            logger.error("", e);
        }
    }


}
