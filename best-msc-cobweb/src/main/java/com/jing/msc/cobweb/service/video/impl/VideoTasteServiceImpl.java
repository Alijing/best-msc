package com.jing.msc.cobweb.service.video.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.util.StringUtils;
import com.jing.msc.cobweb.entity.video.VideoTaste;
import com.jing.msc.cobweb.mapper.video.VideoTasteMapper;
import com.jing.msc.cobweb.service.video.VideoTasteService;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    public IPage<VideoTaste> tastes(String keyword, Integer status, Integer current, Integer size) {
        QueryWrapper<VideoTaste> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like("number", keyword);
            wrapper.like("name", keyword);
            wrapper.like("performer", keyword);
        }
        if (Objects.nonNull(status)) {
            wrapper.eq("status", status);
        }
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public boolean numberValid(Long id, String number) {
        QueryWrapper<VideoTaste> query = new QueryWrapper<>();
        if (Objects.nonNull(id)) {
            VideoTaste byId = getById(id);
            if (Objects.nonNull(byId) && StringUtils.equalsIgnoreCase(byId.getNumber(), number)) {
                return true;
            } else {
                query.eq("number", number);
                return count(query) == 0;
            }
        }
        query.eq("number", number);
        return count(query) == 0;
    }

}
