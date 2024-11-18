package com.jing.msc.cobweb.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.sys.Department;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentNode;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentQuery;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author : jing
 * @packageName : com.fullsee.integratedbis.service
 * @description :
 * @since : 2023-10-16 20:52:26
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 查询单位组织，以树形的结构返回
     *
     * @param query 查询条件
     * @return {@link IPage}<{@link DepartmentNode}>
     */
    IPage<DepartmentNode> departmentTree(DepartmentQuery query);

    /**
     * 查询单位简单组织，以树形的形式返回
     *
     * @return {@link List }<{@link DepartmentNode }>
     */
    List<DepartmentNode> simpleList();

}
