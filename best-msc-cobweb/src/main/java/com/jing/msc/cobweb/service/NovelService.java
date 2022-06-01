package com.jing.msc.cobweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.common.core.base.BaseResp;
import com.jing.msc.cobweb.entity.Novel;
import com.jing.msc.cobweb.entity.vo.NovelVo;

import javax.servlet.http.HttpServletResponse;
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
    BaseResp<List<Novel>> novels(NovelVo novel) throws Exception;

    /**
     * 新增或编辑小说信息
     *
     * @param novel novel
     * @return ID
     * @author jing
     * @date 2022/4/21 15:24
     */
    BaseResp<Long> saveOrUpdateNovel(Novel novel);

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

}
