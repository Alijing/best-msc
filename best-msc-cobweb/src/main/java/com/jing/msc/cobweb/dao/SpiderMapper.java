package com.jing.msc.cobweb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.msc.cobweb.entity.Spider;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.dao
 * @date : 2021/4/23 14:42
 * @description :
 */
public interface SpiderMapper extends BaseMapper<Spider> {

    List<Map<String, Object>> selectAllTable(@Param("tableSchema") String tableSchema);

    List<Map<String, String>> selectAllFiled(@Param("tableName") String tableName);
}
