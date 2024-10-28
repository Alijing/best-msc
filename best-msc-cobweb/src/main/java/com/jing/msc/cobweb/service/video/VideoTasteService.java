package com.jing.msc.cobweb.service.video;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.video.VideoTaste;
import com.jing.msc.cobweb.entity.video.vo.VideoTasteQueryPara;

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
     * @param para 查询参数
     * @return {@link List }<{@link VideoTaste }>
     */
    IPage<VideoTaste> tastes(VideoTasteQueryPara para);


}
