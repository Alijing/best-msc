package com.jing.msc.cobweb.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.cobweb.entity.sys.Menu;
import com.jing.msc.cobweb.entity.sys.vo.MenuItem;
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

    List<MenuItem> selectByRoleId(@Param("roleId") List<Long> roleIds);

}
