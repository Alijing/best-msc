package com.jing.msc.cobweb.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.mapper.sys.RoleMapper;
import com.jing.msc.cobweb.service.sys.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public IPage<Role> roleList(Role role, Long current, Long size) {
        IPage<Role> page = new Page<>(current, size);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (!Objects.isNull(role) && StringUtils.isNotBlank(role.getName())) {
            wrapper.like("name", role.getName());
        }
        return page(page, wrapper);
    }

}
