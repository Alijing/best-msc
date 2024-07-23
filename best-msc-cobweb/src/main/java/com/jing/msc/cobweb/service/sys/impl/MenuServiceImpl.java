package com.jing.msc.cobweb.service.sys.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.common.core.util.JsonUtils;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.MenuName;
import com.jing.msc.cobweb.entity.sys.vo.MenuItem;
import com.jing.msc.cobweb.entity.sys.vo.RouteMetaCustom;
import com.jing.msc.cobweb.entity.sys.vo.RouteRecord;
import com.jing.msc.cobweb.enums.sys.RoleEnum;
import com.jing.msc.cobweb.mapper.sys.MenuMapper;
import com.jing.msc.cobweb.service.sys.MenuService;
import com.jing.msc.security.entity.LoginSpider;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public List<RouteRecord> simpleInfo(Menu query) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new CustomException(ResultEnum.UN_Login);
        }
        LoginSpider principal = (LoginSpider) authentication.getPrincipal();
        List<MenuItem> menuItems = baseMapper.selectSimpleInfo(query, Objects.isNull(principal.getLang()) ? "zh-CN" : principal.getLang());
        if (CollectionUtils.isNotEmpty(menuItems)) {
            return buildRoute(0L, menuItems);
        }
        return Collections.emptyList();
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

    @Override
    public List<RouteRecord> currentUserMenu() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new CustomException(ResultEnum.UN_Login);
        }
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            throw new CustomException(ResultEnum.ROLE_NOT_SET);
        }
        List<Long> roleIds = authorities.stream().map(it -> RoleEnum.getIdByCode(it.getAuthority())).collect(Collectors.toList());
        // 如果是超级管理员，则查询所有菜单
        if (RoleEnum.hasRoleById(RoleEnum.ADMIN, roleIds)) {
            roleIds = null;
        }
        LoginSpider principal = (LoginSpider) authentication.getPrincipal();
        List<MenuItem> menuItems = baseMapper.selectByRoleId(roleIds, Objects.isNull(principal.getLang()) ? "zh-CN" : principal.getLang());
        if (CollectionUtils.isNotEmpty(menuItems)) {
            return buildRoute(0L, menuItems);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean addMenu(RouteRecord route) {
        Menu menu = BeanUtil.copyProperties(route, Menu.class);
        BeanUtil.copyProperties(route.getMeta(), menu);
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
            routeRecord.setChildren(buildRoute(item.getId(), menuItems));
            List<MenuName> menuNames = baseMapper.selectMenuName(item.getId());
            routeRecord.setI18ns(menuNames);
            routes.add(routeRecord);
        }
        return routes;
    }

}
