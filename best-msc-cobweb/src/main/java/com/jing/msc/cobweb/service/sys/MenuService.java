package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.vo.*;

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
     * @param all        是否查询所有菜单
     * @param current    是否查询当前用户菜单
     * @param permission 是否查询当前用户菜单权限
     * @return {@link List }<{@link MenuItem }>
     */
    List<CascaderDictItem> simpleInfo(boolean all, boolean current, boolean permission);

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

    /**
     * 查询当前登录人菜单权限
     *
     * @return {@link List }<{@link RouteRecord }>
     */
    List<RoleMenuPerm> currentPermission();


    /**
     * 根据角色查询菜单id
     *
     * @param roleId   角色Id
     * @param roleCode 角色编码
     * @return {@link List }<{@link Long }>
     */
    List<Long> menuIdsByRole(List<Long> roleId, List<String> roleCode);


    /**
     * 根据角色查询菜单权限
     *
     * @param roleId   角色Id
     * @param roleCode 角色编码
     * @return {@link List }<{@link RoleMenuPerm }>
     */
    List<RoleMenuPerm> menuPermsByRole(List<Long> roleId, List<String> roleCode);

    /**
     * 根据角色查询菜单信息
     *
     * @param roleId     角色Id
     * @param roleCode   角色编码
     * @param permission 是否查询权限
     * @return {@link List }<{@link MenuSimpleInfo }>
     */
    List<MenuSimpleInfo> menuByRole(List<Long> roleId, List<String> roleCode, Boolean permission);

}
