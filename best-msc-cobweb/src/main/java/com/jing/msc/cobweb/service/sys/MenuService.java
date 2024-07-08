package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.vo.RouteRecord;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author : jing
 * @since : 2023-07-21 17:27:53
 */
public interface MenuService extends IService<Menu> {

    /**
     * 初始化菜单
     */
    void initMenu();


    /**
     * 获取当前用户菜单
     *
     * @return 菜单信息
     */
    List<RouteRecord> currentUserMenu();

}
