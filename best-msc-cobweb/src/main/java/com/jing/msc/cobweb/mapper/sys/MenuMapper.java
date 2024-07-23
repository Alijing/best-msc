package com.jing.msc.cobweb.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.MenuName;
import com.jing.msc.cobweb.entity.sys.vo.MenuItem;
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
     * @param query 查询条件
     * @return {@link List }<{@link MenuItem }>
     */
    List<MenuItem> selectSimpleInfo(@Param("query") Menu query, @Param("lang") String lang);

    /**
     * 根据角色id查询菜单
     *
     * @param roleIds 角色id
     * @param lang    语言
     * @return {@link List }<{@link MenuItem }>
     */
    List<MenuItem> selectByRoleId(@Param("roleId") List<Long> roleIds, @Param("lang") String lang);

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

}
