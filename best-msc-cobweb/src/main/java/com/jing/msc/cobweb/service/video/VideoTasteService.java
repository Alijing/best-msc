package com.jing.msc.cobweb.service.video;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.video.VideoTaste;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author : jing
 * @since : 2024-10-25 10:01:03
 */
public interface VideoTasteService extends IService<VideoTaste> {


    /**
     * 查询所有 Taste 视频列表
     *
     * @param keyword 搜索关键字
     * @param status  状态，0：未下载，1：已下载，2：已观看
     * @param current 页码
     * @param size    每页数量
     * @return {@link List }<{@link VideoTaste }>
     */
    IPage<VideoTaste> tastes(String keyword, Integer status, Integer current, Integer size);


    /**
     * 校验 Taste 车牌号 是否唯一
     *
     * @param id     id
     * @param number 车牌号
     * @return boolean
     */
    boolean numberValid(Long id, String number);

}
