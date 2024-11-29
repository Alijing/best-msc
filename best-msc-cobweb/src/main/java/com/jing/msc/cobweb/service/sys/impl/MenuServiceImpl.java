package com.jing.msc.cobweb.service.sys.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.JsonUtils;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.MenuName;
import com.jing.msc.cobweb.entity.sys.vo.*;
import com.jing.msc.cobweb.enums.sys.Permission;
import com.jing.msc.cobweb.mapper.sys.MenuMapper;
import com.jing.msc.cobweb.service.sys.MenuService;
import com.jing.msc.security.utils.UserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author : jing
 * @packageName : com.jing.msc.cobweb.service.impl
 * @description :
 * @since : 2023-07-21 17:27:53
 */
@Service(value = "menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void initI18n() {
        List<Menu> list = list();
        List<MenuName> names = new ArrayList<>();
        for (Menu it : list) {
            if (StringUtils.isBlank(it.getTitle())) {
                continue;
            }
            MenuName name = new MenuName();
            name.setId(IdWorker.getId());
            name.setMenuId(it.getId());
            name.setLang("zh-CN");
            name.setName(it.getTitle());
            names.add(name);
            MenuName name2 = new MenuName();
            name2.setId(IdWorker.getId());
            name2.setMenuId(it.getId());
            name2.setLang("en");
            name2.setName(it.getTitle());
            names.add(name2);
        }
        baseMapper.deleteNameById(list.stream().map(Menu::getId).collect(Collectors.toList()));
        baseMapper.insertMenuName(names);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void initMenu() {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\alijing\\my\\code\\BestMSC\\samp.json"))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            List<MenuItem> list = JsonUtils.toList(json.toString(), MenuItem.class);
            List<Menu> allMenus = findChildren(list, 0L);
            logger.info("初始化菜单成功，共{}条数据", allMenus.size());
        } catch (IOException e) {
            logger.error("读取文件异常", e);
            throw new CustomException(ResultEnum.UNKNOWN_ERROR);
        }
    }

    private List<Menu> findChildren(List<MenuItem> children, Long parentId) {
        List<Menu> temp = new ArrayList<>();
        for (MenuItem it1 : children) {
            Menu menu = BeanUtil.copyProperties(it1, Menu.class);
            menu.setParentId(parentId);
            menu.setPermKey("test:test:edit");
            boolean saved = save(menu);
            if (!saved) {
                throw new CustomException(ResultEnum.SQL_EXCEPTION);
            }
            temp.add(menu);
            if (Objects.nonNull(it1.getChildren())) {
                temp.addAll(findChildren(it1.getChildren(), menu.getId()));
            }
        }
        return temp;
    }

    // ***********************************************************************************************************

    @Override
    public List<CascaderDictItem> simpleInfo(boolean all, boolean current, boolean permission) {
        // 如果是超级管理员，则查询所有菜单
        if (-1 == UserUtil.getUserId()) {
            List<MenuSimpleInfo> menuItems = baseMapper.selectSimpleInfo(UserUtil.getLang());
            if (CollectionUtils.isEmpty(menuItems)) {
                return Collections.emptyList();
            }
            return buildCascaderDictItem(0L, menuItems);
        }

        if (current) {
            List<String> role = UserUtil.getRole();
            if (CollectionUtils.isEmpty(role)) {
                throw new CustomException(ResultEnum.ROLE_NOT_SET);
            }

            List<Long> curMenuIds = menuIdsByRole(null, role);
            if (CollectionUtils.isEmpty(curMenuIds)) {
                return Collections.emptyList();
            }

            List<MenuItem> menuItems = buildTreeAllNode(curMenuIds, null, role, permission);
            if (CollectionUtils.isEmpty(menuItems)) {
                return Collections.emptyList();
            }

            List<MenuSimpleInfo> distinct = new ArrayList<>();
            for (MenuItem item : menuItems) {
                MenuSimpleInfo msi = new MenuSimpleInfo();
                BeanUtil.copyProperties(item, msi);
                distinct.add(msi);
            }
            return buildCascaderDictItem(0L, distinct);
        }

        return Collections.emptyList();
    }

    private List<CascaderDictItem> buildCascaderDictItem(long parentId, List<MenuSimpleInfo> menuItems) {
        if (CollectionUtils.isEmpty(menuItems)) {
            return Collections.emptyList();
        }
        List<CascaderDictItem> dictItems = new ArrayList<>();
        for (MenuSimpleInfo item : menuItems) {
            if (!Objects.equals(item.getParentId(), parentId)) {
                continue;
            }
            CascaderDictItem dict = new CascaderDictItem();
            BeanUtil.copyProperties(item, dict);
            dict.setChildren(buildCascaderDictItem(item.getId(), menuItems));
            dictItems.add(dict);
        }
        return dictItems;
    }

    @Override
    public List<RouteRecord> menuList(String name) {
        Menu me = new Menu();
        me.setName(name);
        List<Long> collect = baseMapper.menuIdByParam(me, UserUtil.getLang());
        if (CollectionUtils.isEmpty(collect)) {
            return Collections.emptyList();
        }
        List<MenuItem> menuItems = buildTreeAllNode(collect, null, null, null);
        if (CollectionUtils.isEmpty(menuItems)) {
            return Collections.emptyList();
        }
        return buildRoute(0L, menuItems);
    }

    @Override
    public List<RouteRecord> currentUserMenu() {
        // 如果是超级管理员，则查询所有菜单
        if (-1 == UserUtil.getUserId()) {
            List<MenuItem> menuItems = baseMapper.selectMenuInfo(UserUtil.getLang());
            return buildRoute(0L, menuItems);
        }

        List<String> roleCodes = new ArrayList<>(UserUtil.getRole());
        List<Long> curMenuIds = menuIdsByRole(null, roleCodes);
        if (CollectionUtils.isEmpty(curMenuIds)) {
            return Collections.emptyList();
        }

        List<MenuItem> tarNodes = buildTreeAllNode(curMenuIds, null, null, null);
        if (CollectionUtils.isEmpty(tarNodes)) {
            return Collections.emptyList();
        }
        return buildRoute(0L, tarNodes);
    }

    /**
     * 查询 当前角色有权限的菜单 并查询其所有上级菜单，以便构建菜单树
     *
     * @param roleIds    角色id
     * @param roleCodes  角色编码
     * @param permission 是否查询权限
     * @return {@link List }<{@link MenuItem }>
     */
    private List<MenuItem> buildTreeAllNode(List<Long> curMenuIds, List<Long> roleIds, List<String> roleCodes, Boolean permission) {
        List<MenuItem> menuItems = baseMapper.selectMenuInfo(UserUtil.getLang());
        if (CollectionUtils.isEmpty(menuItems)) {
            return Collections.emptyList();
        }

        List<RoleMenuPerm> roleMenuPerms = new ArrayList<>();
        if (Objects.nonNull(permission) && permission) {
            roleMenuPerms = baseMapper.selectPermissionsByRole(roleIds, roleCodes, null);
        }

        List<MenuItem> tar = new ArrayList<>();
        for (MenuItem it : menuItems) {
            if (CollectionUtils.isEmpty(curMenuIds)) {
                break;
            }
            if (!curMenuIds.contains(it.getId())) {
                continue;
            }
            curMenuIds.remove(it.getId());

            if (Objects.nonNull(permission) && permission) {
                it.setPermission(findPermission(it.getId(), roleMenuPerms));
            }

            tar.add(it);
            List<MenuItem> parent = findParent(it.getParentId(), menuItems);
            if (CollectionUtils.isEmpty(parent)) {
                continue;
            }

            if (Objects.nonNull(permission) && permission) {
                for (MenuItem pt : parent) {
                    pt.setPermission(findPermission(pt.getId(), roleMenuPerms));
                }
            }

            tar.addAll(parent);
        }
        return tar.stream().distinct().collect(Collectors.toList());
    }

    private List<MenuItem> findParent(Long parentId, List<MenuItem> menuItems) {
        List<MenuItem> tar = new ArrayList<>();
        for (MenuItem it : menuItems) {
            if (!Objects.equals(it.getId(), parentId)) {
                continue;
            }
            tar.add(it);
            tar.addAll(findParent(it.getParentId(), menuItems));
        }
        return tar;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean addMenu(RouteRecord route) {
        Menu menu = BeanUtil.copyProperties(route, Menu.class);
        BeanUtil.copyProperties(route.getMeta(), menu);
        menu.setParentId(Objects.isNull(route.getParentId()) ? 0L : route.getParentId());
        menu.setPermKey("test:info:all");
        boolean update = save(menu);
        if (!update) {
            throw new CustomException(ResultEnum.SQL_EXCEPTION);
        }
        refreshMenuName(route.getI18ns(), menu.getId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean editMenu(RouteRecord route) {
        Menu menu = BeanUtil.copyProperties(route, Menu.class);
        BeanUtil.copyProperties(route.getMeta(), menu);
        boolean update = updateById(menu);
        if (!update) {
            throw new CustomException(ResultEnum.SQL_EXCEPTION);
        }
        refreshMenuName(route.getI18ns(), menu.getId());
        return true;
    }

    private void refreshMenuName(List<MenuName> i18ns, long menuId) {
        if (CollectionUtils.isEmpty(i18ns)) {
            return;
        }
        i18ns.forEach(it -> it.setMenuId(menuId));
        baseMapper.deleteNameById(Collections.singletonList(menuId));
        baseMapper.insertMenuName(i18ns);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean deleteMenu(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new CustomException(ResultEnum.PARAM_ERROR);
        }
        return removeByIds(ids);
    }

    @Override
    public List<RoleMenuPerm> currentPermission() {
        if (-1 == UserUtil.getUserId()) {
            QueryWrapper<Menu> wrapper = new QueryWrapper<>();
            wrapper.select("id");
            wrapper.eq("status", 0);
            List<Menu> menus = list(wrapper);
            if (CollectionUtils.isEmpty(menus)) {
                return Collections.emptyList();
            }
            List<RoleMenuPerm> roleMenuPerms = new ArrayList<>();
            Permission[] values = Permission.values();
            StringJoiner joiner = new StringJoiner(",");
            for (Permission value : values) {
                joiner.add(value.getValue());
            }
            for (Menu menu : menus) {
                roleMenuPerms.add(new RoleMenuPerm(null, menu.getId(), joiner.toString()));
            }
            return roleMenuPerms;
        }

        List<String> role = UserUtil.getRole();
        if (CollectionUtils.isEmpty(role)) {
            return Collections.emptyList();
        }
        List<RoleMenuPerm> permissions = baseMapper.selectPermissionsByRole(null, role, null);
        if (CollectionUtils.isEmpty(permissions)) {
            return Collections.emptyList();
        }
        return permissions;
    }

    @Override
    public List<Long> menuIdsByRole(List<Long> roleId, List<String> roleCode) {
        if (CollectionUtils.isEmpty(roleId) && CollectionUtils.isEmpty(roleCode)) {
            throw new CustomException(ResultEnum.PARAM_ERROR);
        }
        List<Long> permissions = baseMapper.menuIdsByRole(roleId, roleCode);
        if (CollectionUtils.isNotEmpty(permissions)) {
            return permissions;
        }
        return Collections.emptyList();
    }

    @Override
    public List<RoleMenuPerm> menuPermsByRole(List<Long> roleId, List<String> roleCode) {
        if (CollectionUtils.isEmpty(roleId) && CollectionUtils.isEmpty(roleCode)) {
            throw new CustomException(ResultEnum.PARAM_ERROR);
        }
        List<RoleMenuPerm> permissions = baseMapper.selectPermissionsByRole(roleId, roleCode, null);
        if (CollectionUtils.isEmpty(permissions)) {
            return Collections.emptyList();
        }
        return permissions;
    }

    @Override
    public List<MenuSimpleInfo> menuByRole(List<Long> roleId, List<String> roleCode, Boolean permission) {
        if (CollectionUtils.isEmpty(roleId) && CollectionUtils.isEmpty(roleCode)) {
            throw new CustomException(ResultEnum.PARAM_ERROR);
        }

        List<Long> curMenuIds = menuIdsByRole(roleId, roleCode);
        if (CollectionUtils.isEmpty(curMenuIds)) {
            return Collections.emptyList();
        }

        List<MenuItem> menuItems = buildTreeAllNode(curMenuIds, roleId, roleCode, permission);
        if (CollectionUtils.isEmpty(menuItems)) {
            return Collections.emptyList();
        }

        List<MenuSimpleInfo> menuSimpleInfos = new ArrayList<>();
        for (MenuItem item : menuItems) {
            MenuSimpleInfo msi = new MenuSimpleInfo();
            BeanUtil.copyProperties(item, msi);
            menuSimpleInfos.add(msi);
        }
        return simpleMenuInfoTree(0L, menuSimpleInfos);
    }

    private String findPermission(long menuId, List<RoleMenuPerm> roleMenuPerms) {
        if (CollectionUtils.isEmpty(roleMenuPerms)) {
            return null;
        }
        Iterator<RoleMenuPerm> iterator = roleMenuPerms.iterator();
        while (iterator.hasNext()) {
            RoleMenuPerm next = iterator.next();
            if (Objects.equals(next.getMenuId(), menuId)) {
                String permission = next.getPermission();
                iterator.remove();
                return permission;
            }
        }
        return null;
    }

    private List<MenuSimpleInfo> simpleMenuInfoTree(long parentId, List<MenuSimpleInfo> menuItems) {
        if (CollectionUtils.isEmpty(menuItems)) {
            return Collections.emptyList();
        }
        List<MenuSimpleInfo> children = new ArrayList<>();
        for (MenuSimpleInfo item : menuItems) {
            if (!Objects.equals(item.getParentId(), parentId)) {
                continue;
            }
            item.setChildren(simpleMenuInfoTree(item.getId(), menuItems));
            children.add(item);
        }
        return children;
    }

    private List<RouteRecord> buildRoute(Long parentId, List<MenuItem> menuItems) {
        if (CollectionUtils.isEmpty(menuItems)) {
            return Collections.emptyList();
        }
        List<RouteRecord> routes = new ArrayList<>();
        for (MenuItem item : menuItems) {

            if (!Objects.equals(item.getParentId(), parentId)) {
                continue;
            }
            RouteRecord routeRecord = new RouteRecord();
            BeanUtil.copyProperties(item, routeRecord);
            RouteMetaCustom meta = new RouteMetaCustom();
            BeanUtil.copyProperties(item, meta);
            routeRecord.setMeta(meta);
            List<RouteRecord> children = buildRoute(item.getId(), menuItems);
            children.sort(Comparator.comparing(RouteRecord::getSort));
            routeRecord.setChildren(children);
            List<MenuName> menuNames = baseMapper.selectMenuName(item.getId());
            routeRecord.setI18ns(menuNames);
            routes.add(routeRecord);
        }
        routes.sort(Comparator.comparing(RouteRecord::getSort));
        return routes;
    }

}
