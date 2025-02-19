package com.jing.msc.cobweb.service;

import com.jing.msc.cobweb.entity.test.ResGroup;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author : jing
 * @since : 2024/12/10 11:32
 */
public interface TestService {


    //---------------------------------------------------------------------------------------

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


    /**
     * 获取 json key
     *
     * @param json json
     * @return {@link List }<{@link String }>
     */
    List<String> getJsonKeys(String json);


}
