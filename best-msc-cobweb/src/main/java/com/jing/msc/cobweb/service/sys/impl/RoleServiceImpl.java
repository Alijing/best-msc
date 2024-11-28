package com.jing.msc.cobweb.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.entity.sys.RoleMenuPermission;
import com.jing.msc.cobweb.entity.sys.RoleMenuRef;
import com.jing.msc.cobweb.entity.sys.vo.MenuSimpleInfo;
import com.jing.msc.cobweb.entity.sys.vo.RoleInfo;
import com.jing.msc.cobweb.entity.sys.vo.RoleMenuPerm;
import com.jing.msc.cobweb.enums.sys.Permission;
import com.jing.msc.cobweb.mapper.sys.RoleMapper;
import com.jing.msc.cobweb.service.sys.MenuService;
import com.jing.msc.cobweb.service.sys.RoleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author : jing
 * @packageName : com.jing.msc.cobweb.service.impl
 * @description :
 * @since : 2023-07-21 17:27:54
 */
@Service(value = "roleService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "menuService")
    private MenuService menuService;

    @Override
    public IPage<Role> roleList(Role role, Long current, Long size) {
        IPage<Role> page = new Page<>(current, size);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (!Objects.isNull(role) && StringUtils.isNotBlank(role.getName())) {
            wrapper.like("name", role.getName());
        }
        return page(page, wrapper);
    }

    @Override
    public List<Role> simpleList() {
        QueryWrapper<Role> query = new QueryWrapper<>();
        query.select("id", "name");
        query.eq("status", 0);
        query.orderByDesc("create_time");
        List<Role> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public Long saveOrUpdate(RoleInfo target) {
        Role role = new Role();
        if (Objects.nonNull(target.getId())) {
            role.setId(target.getId());
        }
        role.setName(target.getName());
        role.setCode(target.getCode());
        role.setStatus(target.getStatus());
        role.setRemark(target.getRemark());
        boolean update = saveOrUpdate(role);
        if (!update) {
            throw new RuntimeException("保存角色信息失败");
        }

        if (CollectionUtils.isEmpty(target.getPermission())) {
            return role.getId();
        }
        refreshRoleMenuPermission(role.getId(), target.getPermission());
        return role.getId();
    }

    private void refreshRoleMenuPermission(Long id, List<RoleMenuPerm> permission) {
        if (Objects.isNull(id)) {
            return;
        }
        baseMapper.deleteRoleMenuRef(id);
        baseMapper.deleteRoleMenuPermissionRef(id);
        if (CollectionUtils.isEmpty(permission)) {
            return;
        }

        List<RoleMenuRef> rmfs = new ArrayList<>();
        List<RoleMenuPermission> rmps = new ArrayList<>();
        for (RoleMenuPerm perm : permission) {
            RoleMenuRef rmf = new RoleMenuRef();
            rmf.setId(IdWorker.getId());
            rmf.setRoleId(id);
            rmf.setMenuId(perm.getMenuId());
            rmfs.add(rmf);

            if (StringUtils.isBlank(perm.getPermission())) {
                continue;
            }

            String[] split = perm.getPermission().split(",");
            for (String sp : split) {
                RoleMenuPermission rmp = new RoleMenuPermission();
                rmp.setId(IdWorker.getId());
                rmp.setRoleId(id);
                rmp.setMenuId(perm.getMenuId());
                rmp.setPermission(Permission.getByValue(sp));
                rmps.add(rmp);
            }

        }

        if (CollectionUtils.isNotEmpty(rmfs)) {
            baseMapper.insertRoleMenuRef(rmfs);
        }

        if (CollectionUtils.isNotEmpty(rmps)) {
            baseMapper.insertRoleMenuPermissionRef(rmps);
        }

    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean removed = removeByIds(ids);
        if (!removed) {
            throw new RuntimeException("删除角色失败");
        }
        ids.forEach(id -> refreshRoleMenuPermission(id, null));
        return true;
    }

    @Override
    public boolean codeValid(Long id, String code) {
        QueryWrapper<Role> query = new QueryWrapper<>();
        if (Objects.nonNull(id)) {
            Role byId = getById(id);
            if (Objects.nonNull(byId) && Objects.equals(byId.getCode(), code)) {
                return true;
            } else {
                query.eq("code", code);
                return count(query) == 0;
            }
        }
        query.eq("code", code);
        return count(query) == 0;
    }

    @Override
    public RoleInfo roleInfo(long id) {
        Role role = getById(id);
        Assert.notNull(role, "角色不存在");

        RoleInfo ret = new RoleInfo();
        ret.setId(role.getId());
        ret.setName(role.getName());
        ret.setCode(role.getCode());
        ret.setStatus(role.getStatus());
        ret.setRemark(role.getRemark());

        List<RoleMenuPerm> roleMenuPerms = menuService.menuPermsByRole(Collections.singletonList(id), null);
        ret.setPermission(roleMenuPerms);
        return ret;
    }

    @Override
    public List<MenuSimpleInfo> roleMenuRef(long id, Boolean permission) {
        List<MenuSimpleInfo> menuSimpleInfos = menuService.menuByRole(Collections.singletonList(id), null, permission);
        if (CollectionUtils.isEmpty(menuSimpleInfos)) {
            return Collections.emptyList();
        }
        return menuSimpleInfos;
    }

}
