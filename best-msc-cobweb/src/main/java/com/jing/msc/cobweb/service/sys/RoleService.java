package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.Role;

import java.util.List;

/**
 * <p>
 * 系统角色信息表 服务类
 * </p>
 *
 * @author : jing
 * @since : 2024-07-04 16:21:35
 */
public interface RoleService extends IService<Role> {


    /**
     * @param role    查询条件
     * @param current 当前页
     * @param size    每页大小
     * @return {@link List }<{@link Role }>
     */
    IPage<Role> roleList(Role role, Long current, Long size);

}
