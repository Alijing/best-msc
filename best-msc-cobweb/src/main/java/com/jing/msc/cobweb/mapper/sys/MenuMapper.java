package com.jing.msc.cobweb.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.MenuName;
import com.jing.msc.cobweb.entity.sys.vo.MenuItem;
import com.jing.msc.cobweb.entity.sys.vo.MenuSimpleInfo;
import com.jing.msc.cobweb.entity.sys.vo.RoleMenuPerm;
import com.jing.msc.cobweb.entity.sys.vo.RouteRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author : jing
 * @packageName : com.jing.msc.cobweb.mapper
 * @description :
 * @since : 2023-07-21 17:27:53
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询简单菜单信息
     *
     * @return {@link List }<{@link MenuItem }>
     */
    List<MenuSimpleInfo> selectSimpleInfo(@Param("lang") String lang);

    /**
     * 根据角色id查询菜单
     *
     * @param roleIds 角色id
     * @param lang    语言
     * @return {@link List }<{@link MenuItem }>
     */
    List<MenuItem> selectByRole(
            @Param("menuId") List<Long> menuId,
            @Param("roleId") List<Long> roleIds,
            @Param("roleCodes") List<String> roleCodes,
            @Param("lang") String lang
    );

    /**
     * 删除菜单名称
     *
     * @param id 菜单id
     */
    void deleteNameById(@Param("id") List<Long> id);

    /**
     * 批量插入菜单名称
     *
     * @param list 菜单名称
     */
    void insertMenuName(@Param("list") List<MenuName> list);

    /**
     * 根据菜单id查询菜单名称
     *
     * @param id 菜单id
     * @return {@link List }<{@link MenuName }>
     */
    List<MenuName> selectMenuName(@Param("id") Long id);

    /**
     * 根据角色查询菜单id
     *
     * @param roleIds   角色id
     * @param roleCodes 角色编码
     * @return {@link List }<{@link Long }>
     */
    List<Long> menuIdsByRole(@Param("roleIds") List<Long> roleIds, @Param("roleCodes") List<String> roleCodes);

    /**
     * 查询当前登录人角色与菜单的权限
     *
     * @param roleIds   角色id
     * @param roleCodes 角色编码
     * @return {@link List }<{@link RouteRecord }>
     */
    List<RoleMenuPerm> selectPermissionsByRole(
            @Param("roleIds") List<Long> roleIds,
            @Param("roleCodes") List<String> roleCodes,
            @Param("menuIds") List<Long> menuIds
    );


}
