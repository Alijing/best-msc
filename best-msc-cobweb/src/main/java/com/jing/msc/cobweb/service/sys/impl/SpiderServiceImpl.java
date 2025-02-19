package com.jing.msc.cobweb.service.sys.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.msc.cobweb.entity.sys.Department;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.entity.sys.SpiderDepartmentRef;
import com.jing.msc.cobweb.entity.sys.SpiderRoleRef;
import com.jing.msc.cobweb.entity.sys.vo.SpiderInfo;
import com.jing.msc.cobweb.entity.sys.vo.SpiderQueryParam;
import com.jing.msc.cobweb.mapper.sys.SpiderMapper;
import com.jing.msc.cobweb.service.sys.DepartmentService;
import com.jing.msc.cobweb.service.sys.RoleService;
import com.jing.msc.cobweb.service.sys.SpiderService;
import com.jing.msc.security.entity.Spider;
import com.jing.msc.security.utils.UserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.service.impl
 * @date : 2021/4/23 14:59
 * @description :
 */
@Service(value = "spiderService")
@Transactional(rollbackFor = Exception.class)
public class SpiderServiceImpl extends ServiceImpl<SpiderMapper, Spider> implements SpiderService {

    private final Logger logger = LoggerFactory.getLogger(SpiderServiceImpl.class);

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    @Resource(name = "roleService")
    private RoleService roleService;

    @Override
    public IPage<SpiderInfo> list(SpiderQueryParam query) {
        IPage<SpiderInfo> iPage = new Page<>(query.getPageIndex(), query.getPageSize());
        List<SpiderInfo> infos = baseMapper.selectByParams(iPage, query);
        if (CollectionUtils.isEmpty(infos)) {
            iPage.setRecords(new ArrayList<>());
            return iPage;
        }

        List<Role> roles = roleService.list();
        List<Department> departments = departmentService.list();
        for (SpiderInfo it : infos) {
            buildRoleInfo(it, roles);
            buildDepartmentInfo(it, departments);
        }

        iPage.setRecords(infos);
        return iPage;
    }

    private void buildDepartmentInfo(SpiderInfo target, List<Department> departments) {
        if (StringUtils.isBlank(target.getDepartment()) || CollectionUtils.isEmpty(departments)) {
            return;
        }
        String[] split = target.getDepartment().split(",");
        if (split.length == 0) {
            return;
        }

        List<Long> ids = new ArrayList<>();
        StringJoiner joiner = new StringJoiner(",");
        for (String id : split) {
            for (Department it : departments) {
                if (!it.getId().equals(Long.valueOf(id))) {
                    continue;
                }
                ids.add(it.getId());
                joiner.add(it.getName());
            }
        }
        target.setDepartmentId(ids);
        target.setDepartment(joiner.toString());
    }

    private void buildRoleInfo(SpiderInfo target, List<Role> roles) {
        if (StringUtils.isBlank(target.getRoleName()) || CollectionUtils.isEmpty(roles)) {
            return;
        }
        String[] split = target.getRoleName().split(",");
        if (split.length == 0) {
            return;
        }
        List<Long> roleIds = new ArrayList<>();
        StringJoiner joiner = new StringJoiner(",");
        for (String roleId : split) {
            for (Role rl : roles) {
                if (!rl.getId().equals(Long.valueOf(roleId))) {
                    continue;
                }
                roleIds.add(rl.getId());
                joiner.add(rl.getName());
            }
        }
        target.setRoleId(roleIds);
        target.setRoleName(joiner.toString());
    }

    @Override
    public boolean spiderSaveOrUpdate(SpiderInfo target) {
        Spider spider = new Spider();
        if (!Objects.isNull(target.getId())) {
            spider.setId(target.getId());
        } else {
            spider.setPassword(passwordEncoder.encode(target.getAccount()));
        }
        spider.setAccount(target.getAccount());
        spider.setName(target.getName());
        spider.setPhone(target.getPhone());

        boolean update = saveOrUpdate(spider);
        if (!update) {
            throw new CustomException(ResultEnum.SQL_EXCEPTION);
        }
        refreshRelation(spider.getId(), null, target);
        return true;
    }

    @Override
    public boolean delete(List<Long> ids) {
        Assert.notNull(ids, "请选择需要删除的用户");
        Assert.isTrue(UserUtil.hasRole("ADMIN"), "【管理员】权限才能删除");
        boolean removed = removeByIds(ids);
        if (!removed) {
            throw new CustomException(ResultEnum.SQL_EXCEPTION);
        }
        refreshRelation(null, ids, null);
        return true;
    }

    private void refreshRelation(Long id, List<Long> ids, SpiderInfo target) {
        if (CollectionUtils.isEmpty(ids)) {
            ids = new ArrayList<>();
        }
        if (Objects.nonNull(id)) {
            ids.add(id);
        }

        if (CollectionUtils.isNotEmpty(ids)) {
            baseMapper.deleteSpiderDepartmentRef(ids);
            baseMapper.deleteSpiderRoleRef(ids);
        }

        if (Objects.isNull(target)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(target.getRoleId())) {
            List<SpiderRoleRef> collect = target.getRoleId().stream()
                    .map(it -> new SpiderRoleRef(IdWorker.getId(), id, it))
                    .collect(Collectors.toList());
            boolean insert = SqlHelper.retBool(baseMapper.insertSpiderRoleRef(collect));
            if (!insert) {
                throw new CustomException(ResultEnum.SQL_EXCEPTION);
            }
        }

        if (CollectionUtils.isNotEmpty(target.getDepartmentId())) {
            List<SpiderDepartmentRef> collect = target.getDepartmentId().stream()
                    .map(it -> new SpiderDepartmentRef(IdWorker.getId(), id, it))
                    .collect(Collectors.toList());
            boolean insert = SqlHelper.retBool(baseMapper.insertSpiderDepartmentRef(collect));
            if (!insert) {
                throw new CustomException(ResultEnum.SQL_EXCEPTION);
            }
        }

    }

}




