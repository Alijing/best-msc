package com.jing.msc.cobweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.mapper.NovelChapterMapper;
import com.jing.msc.cobweb.entity.NovelChapter;
import com.jing.msc.cobweb.service.NovelChapterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service.impl
 * @date : 2021/11/12 10:26
 * @description :
 */
@Service("novelChapterService")
@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
public class NovelChapterServiceImpl extends ServiceImpl<NovelChapterMapper, NovelChapter> implements NovelChapterService {

    @Override
    public List<NovelChapter> queryChapterByPath(String path) {
        if (StringUtils.isBlank(path)) {
            return new ArrayList<>();
        }
        List<NovelChapter> chapters = baseMapper.queryChapterByPath(path);
        if (null == chapters || chapters.size() < 1) {
            return new ArrayList<>();
        }
        return chapters;
    }

}
