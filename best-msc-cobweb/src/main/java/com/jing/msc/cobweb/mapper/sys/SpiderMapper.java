package com.jing.msc.cobweb.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jing.msc.cobweb.entity.sys.SpiderDepartmentRef;
import com.jing.msc.cobweb.entity.sys.SpiderRoleRef;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentNode;
import com.jing.msc.cobweb.entity.sys.vo.SpiderInfo;
import com.jing.msc.cobweb.entity.sys.vo.SpiderQueryParam;
import com.jing.msc.security.entity.Spider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.dao
 * @date : 2021/4/23 14:42
 * @description :
 */
public interface SpiderMapper extends BaseMapper<Spider> {

    /**
     * 查询用户信息
     *
     * @param iPage 分页
     * @param query 查询条件
     * @return {@link List}<{@link DepartmentNode}>
     */
    List<SpiderInfo> selectByParams(IPage<SpiderInfo> iPage, @Param("query") SpiderQueryParam query);

    /**
     * 删除用户部门关联表数据
     *
     * @param spiderId 用户id
     * @return int 删除数量
     */
    int deleteSpiderDepartmentRef(@Param("spiderId") List<Long> spiderId);

    /**
     * 插入用户部门关联表数据
     *
     * @param ref 用户部门关联表数据
     * @return int
     */
    int insertSpiderDepartmentRef(@Param("ref") List<SpiderDepartmentRef> ref);

    /**
     * 删除用户角色关联表数据
     *
     * @param spiderId 用户id
     * @return int 删除数量
     */
    int deleteSpiderRoleRef(@Param("spiderId") List<Long> spiderId);

    /**
     * 插入用户角色关联表数据
     *
     * @param ref 用户角色关联表数据
     * @return int
     */
    int insertSpiderRoleRef(@Param("ref") List<SpiderRoleRef> ref);

}
