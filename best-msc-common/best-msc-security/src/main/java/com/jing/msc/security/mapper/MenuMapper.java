package com.jing.msc.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.security.entity.Menu;
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
 * @since : 2023-07-21 17:27:53
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过 用户Id 查询权限信息
     *
     * @param spiderId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> selectPermsByUserId(@Param("spiderId") Long spiderId);

}
