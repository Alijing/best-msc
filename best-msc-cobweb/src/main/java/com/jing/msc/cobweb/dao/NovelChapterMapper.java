package com.jing.msc.cobweb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.cobweb.entity.NovelChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.mapper
 * @date : 2021/11/11 18:06
 * @description :
 */
public interface NovelChapterMapper extends BaseMapper<NovelChapter> {

    /**
     * 通过 path 查询所有章节的信息
     *
     * @param path 某一章节的地址
     * @return 所有章节的信息
     * @author jing
     * @date 2021/11/12 15:29
     */
    List<NovelChapter> queryChapterByPath(@Param("path") String path);

}