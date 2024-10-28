package com.jing.msc.cobweb.service.video.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.entity.video.VideoTaste;
import com.jing.msc.cobweb.entity.video.vo.VideoTasteQueryPara;
import com.jing.msc.cobweb.mapper.video.VideoTasteMapper;
import com.jing.msc.cobweb.service.video.VideoTasteService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author : jing
 * @since : 2024-10-25 10:01:03
 */
@Service(value = "videoTasteService")
public class VideoTasteServiceImpl extends ServiceImpl<VideoTasteMapper, VideoTaste> implements VideoTasteService {

    @Override
    public IPage<VideoTaste> tastes(VideoTasteQueryPara para) {
        QueryWrapper<VideoTaste> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        if (null != para.getNumber()) {
            wrapper.like("number", para.getNumber());
        }
        if (null != para.getName()) {
            wrapper.like("name", para.getName());
        }
        if (null != para.getPerformer()) {
            wrapper.like("performer", para.getPerformer());
        }
        return page(new Page<>(para.getCurrentPage(), para.getPageSize()), wrapper);
    }

}
