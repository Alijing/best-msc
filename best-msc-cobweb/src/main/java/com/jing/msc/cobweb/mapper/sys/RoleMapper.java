package com.jing.msc.cobweb.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.entity.sys.RoleMenuPermission;
import com.jing.msc.cobweb.entity.sys.RoleMenuRef;
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
 * @since : 2023-07-21 17:27:54
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 删除角色菜单关联表
     *
     * @param roleId 角色id
     */
    void deleteRoleMenuRef(@Param("roleId") long roleId);

    /**
     * 批量插入角色菜单关联表
     *
     * @param refs 角色菜单关联表
     */
    void insertRoleMenuRef(@Param("refs") List<RoleMenuRef> refs);

    /**
     * 删除角色菜单权限关联表
     *
     * @param roleId 角色id
     */
    void deleteRoleMenuPermissionRef(@Param("roleId") long roleId);

    /**
     * 批量插入角色菜单权限关联表
     *
     * @param refs 角色菜单权限关联表
     */
    void insertRoleMenuPermissionRef(@Param("refs") List<RoleMenuPermission> refs);

}
