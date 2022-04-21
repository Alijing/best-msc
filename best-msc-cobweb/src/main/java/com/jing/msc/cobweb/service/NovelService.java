package com.jing.msc.cobweb.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.Novel;

import javax.servlet.http.HttpServletResponse;

/**
 * @author : jing
 * @projectName : magic
 * @packageName : com.jing.magic.service
 * @date : 2021/11/11 16:50
 * @description :
 */
public interface NovelService extends IService<Novel> {

    /**
     * 下载小说
     *
     * @param novelId  小说Id
     * @param response 响应
     * @author jing
     * @date 2021/11/12 16:40
     */
    void download(Long novelId, HttpServletResponse response);


    void changeChapterName(Long novelId, HttpServletResponse response);
}
