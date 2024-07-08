package com.jing.msc.cobweb.service.sys.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.mapper.sys.DepartmentMapper;
import com.jing.msc.cobweb.entity.sys.Department;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentNode;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentQuery;
import com.jing.msc.cobweb.service.sys.DepartmentService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author : jing
 * @packageName : com.fullsee.integratedbis.service.impl
 * @description :
 * @since : 2023-10-16 20:52:26
 */
@Service(value = "departmentService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public IPage<DepartmentNode> departmentTree(DepartmentQuery query) {
        IPage<DepartmentNode> page = new Page<>();
        if (!Objects.isNull(query.getCurrentPage()) && !Objects.isNull(query.getPageSize())) {
            page = new Page<>(query.getCurrentPage(), query.getPageSize());
        }
        query.setParentId(-1L);
        List<DepartmentNode> nodes = baseMapper.selectByParams(page, query);
        if (CollectionUtils.isEmpty(nodes)) {
            return page;
        }
        for (DepartmentNode node : nodes) {
            findChildren(node, query);
        }
        page.setRecords(nodes);
        return page;
    }

    /**
     * 寻找孩子
     *
     * @param node  节点
     * @param query 查询条件
     */
    private void findChildren(DepartmentNode node, DepartmentQuery query) {
        query.setParentId(node.getId());
        List<DepartmentNode> children = baseMapper.selectByParams(null, query);
        if (CollectionUtils.isNotEmpty(children)) {
            for (DepartmentNode child : children) {
                findChildren(child, query);
            }
            node.setChildren(children);
        }
    }

}
