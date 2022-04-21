package com.jing.msc.cobweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.NovelChapter;

import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service
 * @date : 2021/11/12 10:26
 * @description :
 */
public interface NovelChapterService extends IService<NovelChapter> {

    /**
     * 通过 path 查询所有章节的信息
     *
     * @param path 某一章节的地址
     * @return 所有章节的信息
     * @author jing
     * @date 2021/11/12 15:29
     */
    List<NovelChapter> queryChapterByPath(String path);
}
