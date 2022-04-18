package com.jing.msc.cobweb.service;

import java.util.List;
import java.util.Map;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.service
 * @date : 2021/4/23 14:42
 * @description : 系统登录用户相关服务类
 */
public interface SpiderService {

    void generate(Integer step);


    List<Map<String, Object>> selectAllTable(String tableSchema);

    List<Map<String, String>> selectAllFiled(String tableName);
}
