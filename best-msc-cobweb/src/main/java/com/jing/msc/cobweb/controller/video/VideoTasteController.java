package com.jing.msc.cobweb.controller.video;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jing.common.core.base.BasePageResp;
import com.jing.common.core.base.BaseResp;
import com.jing.common.log.aspect.WebLog;
import com.jing.msc.cobweb.controller.book.NovelController;
import com.jing.msc.cobweb.entity.video.VideoTaste;
import com.jing.msc.cobweb.service.video.VideoTasteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author : jing
 * @since : 2024-10-25 10:01:03
 */
@RestController
@ApiSupport(order = 20)
@Tag(name = "视频相关接口", description = "视频相关接口描述")
@RequestMapping("video/taste")
public class VideoTasteController {

    private final Logger logger = LoggerFactory.getLogger(NovelController.class);

    @Resource(name = "videoTasteService")
    private VideoTasteService tasteService;

    @WebLog(description = "查询所有 Taste 视频列表")
    @Operation(summary = "查询所有 Taste 视频列表")
    @ApiOperationSupport(author = "Jing", order = 1)
    @GetMapping("/list")
    public BaseResp<List<VideoTaste>> tastes(@RequestParam(value = "keyword", required = false) String keyword,
                                             @RequestParam(value = "status", required = false) Integer status,
                                             @RequestParam(value = "pageIndex") Integer current,
                                             @RequestParam(value = "pageSize") Integer size) {
        IPage<VideoTaste> iPage = tasteService.tastes(keyword,status, current, size);
        return BasePageResp.ok(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @WebLog(description = "新增 Taste 视频")
    @Operation(summary = "新增 Taste 视频")
    @ApiOperationSupport(author = "Jing", order = 2)
    @PostMapping("/info")
    public BaseResp<Boolean> add(@RequestBody VideoTaste para) {
        return BaseResp.ok(tasteService.save(para));
    }


    @WebLog(description = "修改 Taste 视频")
    @Operation(summary = "修改 Taste 视频")
    @ApiOperationSupport(author = "Jing", order = 3)
    @PutMapping("/info")
    public BaseResp<Boolean> edit(@RequestBody VideoTaste para) {
        return BaseResp.ok(tasteService.updateById(para));
    }

    @WebLog(description = "查询 Taste 视频")
    @Operation(summary = "查询 Taste 视频")
    @ApiOperationSupport(author = "Jing", order = 4)
    @GetMapping("/info/{id}")
    public BaseResp<VideoTaste> get(@PathVariable Long id) {
        return BaseResp.ok(tasteService.getById(id));
    }


    @WebLog(description = "删除 Taste 视频")
    @Operation(summary = "删除 Taste 视频")
    @ApiOperationSupport(author = "Jing", order = 5)
    @DeleteMapping("/info")
    public BaseResp<Boolean> delete(@RequestParam(value = "ids") List<Long> ids) {
        return BaseResp.ok(tasteService.removeBatchByIds(ids));
    }

    @WebLog(description = "校验 Taste 车牌号 是否唯一")
    @Operation(summary = "校验 Taste 车牌号 是否唯一")
    @ApiOperationSupport(author = "Jing", order = 5)
    @GetMapping("/number/valid")
    public BaseResp<Boolean> numberValid(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "value") String number) {
        boolean unique = tasteService.numberValid(id, number);
        return unique ? BaseResp.ok(true) : BaseResp.ok(false, "车牌号【" + number + "】已存在");
    }

}

