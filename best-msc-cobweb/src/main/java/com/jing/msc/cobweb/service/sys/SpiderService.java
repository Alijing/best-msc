package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.vo.SpiderInfo;
import com.jing.msc.cobweb.entity.sys.vo.SpiderQueryParam;
import com.jing.msc.security.entity.Spider;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.service
 * @date : 2021/4/23 14:42
 * @description : 系统登录用户相关服务类
 */
public interface SpiderService extends IService<Spider> {

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return {@link IPage }<{@link SpiderInfo }>
     */
    IPage<SpiderInfo> list(SpiderQueryParam query);

    /**
     * 新增或修改
     *
     * @param target 用户信息
     * @return {@link IPage }<{@link SpiderInfo }>
     */
    boolean spiderSaveOrUpdate(SpiderInfo target);

    /**
     * 删除
     *
     * @param ids id集合
     * @return boolean
     */
    boolean delete(List<Long> ids);

}
