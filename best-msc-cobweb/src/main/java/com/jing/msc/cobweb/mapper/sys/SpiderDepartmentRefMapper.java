package com.jing.msc.cobweb.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jing.msc.cobweb.entity.sys.SpiderDepartmentRef;
import com.jing.msc.cobweb.entity.sys.vo.DepartmentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : jing
 * @since : 2024/11/18 16:44
 */
public interface SpiderDepartmentRefMapper extends BaseMapper<SpiderDepartmentRef> {

    /**
     * 分页查询部门下用户Id
     *
     * @param iPage 分页
     * @param query 查询条件
     * @return {@link List}<{@link SpiderDepartmentRef}>
     */
    List<SpiderDepartmentRef> selectByParams(IPage<SpiderDepartmentRef> iPage, @Param("departmentId") DepartmentQuery query);

}
