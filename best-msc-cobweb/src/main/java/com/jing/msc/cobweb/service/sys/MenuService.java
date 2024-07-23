package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.vo.MenuItem;
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
     * 菜单名称国际化
     */
    void initI18n();

    /**
     * 初始化菜单
     */
    void initMenu();

    /**
     * 查询简单菜单信息
     *
     * @param query 查询条件
     * @return {@link List }<{@link MenuItem }>
     */
    List<RouteRecord> simpleInfo(Menu query);

    /**
     * 获取当前用户菜单
     *
     * @return 菜单信息
     */
    List<RouteRecord> currentUserMenu();

    /**
     * 新增菜单
     *
     * @param route 菜单信息
     * @return boolean 是否成功
     */
    boolean addMenu(RouteRecord route);

    /**
     * 编辑菜单
     *
     * @param route 菜单信息
     * @return boolean 是否成功
     */
    boolean editMenu(RouteRecord route);


    /**
     * 删除菜单
     *
     * @param ids 菜单id
     * @return boolean 是否成功
     */
    boolean deleteMenu(List<Long> ids);

}
