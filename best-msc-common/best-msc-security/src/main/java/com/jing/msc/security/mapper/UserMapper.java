package com.jing.msc.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface UserMapper extends BaseMapper<Spider> {


    /**
     * 通过 用户Id 查询权限信息
     *
     * @param spiderId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> selectPermsByUserId(@Param("spiderId") Long spiderId);

    /**
     * 通过 用户Id 查询当前拥有的角色信息
     *
     * @param spiderId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> selectRoleByUserId(@Param("spiderId") Long spiderId);

}
