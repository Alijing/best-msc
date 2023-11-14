package com.jing.msc.cobweb.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jing.msc.cobweb.entity.sys.Department;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentNode;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author : jing
 * @packageName : com.fullsee.integratedbis.mapper
 * @description :
 * @since : 2023-10-16 20:52:26
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 查询单位组织，以树形的结构返回
     *
     * @param iPage 分页
     * @param query 查询条件
     * @return {@link List}<{@link DepartmentNode}>
     */
    List<DepartmentNode> selectByParams(IPage<DepartmentNode> iPage, @Param("query") DepartmentQuery query);

}
