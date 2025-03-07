package com.jing.msc.cobweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.book.Novel;
import com.jing.msc.cobweb.entity.vo.NovelVo;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service
 * @date : 2021/11/11 16:50
 * @description :
 */
public interface NovelService extends IService<Novel> {

    /**
     * 通过 小说信息查询 小说列表
     *
     * @param novel novel
     * @return 小说列表
     * @author jing
     * @date 2022/4/21 15:16
     */
    BaseResp<List<Novel>> novels(NovelVo novel);

    /**
     * 通过 id 批量删除
     *
     * @param ids 待删除的 id
     * @return 是否成功
     * @author jing
     * @date 2023/3/31 16:37
     */
    BaseResp<Boolean> batchDelete(List<Long> ids);

    /**
     * 下载小说
     *
     * @param novelId  小说Id
     * @param response 响应
     * @author jing
     * @date 2021/11/12 16:40
     */
    void download(Long novelId, HttpServletResponse response);

    /**
     * 修改章节名称
     *
     * @param novelId 小说Id
     * @return 是否成功
     * @author jing
     * @date 2022/4/22 16:49
     */
    BaseResp<Boolean> changeChapterName(Long novelId);

    /**
     * 爬取章节
     *
     * @param novelId 小说Id
     * @return {@link Integer } 爬取数量
     */
    Integer crawlChapter(Long novelId) throws MalformedURLException;

    /**
     * 爬取章节内容
     *
     * @param novelId 小说Id
     */
    void crawlContent(Long novelId);

}
