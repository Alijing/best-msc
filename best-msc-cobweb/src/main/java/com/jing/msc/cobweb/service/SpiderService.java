package com.jing.msc.cobweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.test.ResGroup;
import com.jing.msc.security.entity.Spider;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.service
 * @date : 2021/4/23 14:42
 * @description : 系统登录用户相关服务类
 */
public interface SpiderService extends IService<Spider> {

    /**
     * 读取 excel 文件中的数据
     *
     * @param type number
     * @param file number
     * @author jing
     * @date 2023/4/19 15:00
     */
    void readResGroupExcel(Integer type, MultipartFile file);

    /**
     * 通过 导入的 资源组数据生成 区域 sql
     *
     * @param groups   资源组数据
     * @param startIdx 开始下标
     * @param parentId 父节点Id
     * @author jing
     * @date 2023/4/19 15:24
     */
    void buildInsertSql(List<ResGroup> groups, int startIdx, long parentId);

    /**
     * 生成 sql
     *
     * @param startId      开始id
     * @param targetNodeId 开始id
     * @param fieldNum     字段个数
     * @author jing
     * @date 2023/5/8 16:44
     */
    void generateSql(int startId, int targetNodeId, int fieldNum);
}
