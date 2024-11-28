package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.Role;
import com.jing.msc.cobweb.entity.sys.vo.MenuSimpleInfo;
import com.jing.msc.cobweb.entity.sys.vo.RoleInfo;

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

    /**
     * 获取角色简单信息
     *
     * @return {@link List }<{@link Role }>
     */
    List<Role> simpleList();

    /**
     * 新增或修改角色信息
     *
     * @param target 目标对象
     * @return long 角色Id
     */
    Long saveOrUpdate(RoleInfo target);

    /**
     * 删除
     *
     * @param ids id集合
     * @return boolean
     */
    boolean delete(List<Long> ids);

    /**
     * 验证角色编码是否唯一
     *
     * @param id   角色Id
     * @param code 角色编码
     * @return boolean true：唯一，false：已存在
     */
    boolean codeValid(Long id, String code);

    /**
     * 查询角色信息
     *
     * @param id id
     * @return {@link RoleInfo }
     */
    RoleInfo roleInfo(long id);

    /**
     * 查询角色菜单关联信息
     *
     * @param id         id
     * @param permission 是否查询权限
     * @return {@link List }<{@link MenuSimpleInfo }>
     */
    List<MenuSimpleInfo> roleMenuRef(long id, Boolean permission);

}
