package com.jing.msc.cobweb.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.msc.cobweb.entity.test.ResGroup;
import com.jing.msc.cobweb.listener.ResGroupReadListener;
import com.jing.msc.cobweb.service.SpiderService;
import com.jing.msc.security.entity.Spider;
import com.jing.msc.security.mapper.SpiderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.msc.cobweb.service.impl
 * @date : 2021/4/23 14:59
 * @description :
 */
@Service(value = "spiderService")
@Transactional(rollbackFor = Exception.class)
public class SpiderServiceImpl extends ServiceImpl<SpiderMapper, Spider> implements SpiderService {

    protected final Logger logger = LoggerFactory.getLogger(SpiderServiceImpl.class);

    @Override
    public void readResGroupExcel(Integer type, MultipartFile file) {
        try {
            logger.info("--------- type : {} ", type);
            EasyExcel.read(file.getInputStream(), ResGroup.class, new ResGroupReadListener(this)).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(ResultEnum.IOException);
        }
    }

    @Override
    public void buildInsertSql(List<ResGroup> groups, int startIdx, long parentId) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < groups.size(); i++) {
            ResGroup it = groups.get(i);
            builder.append("INSERT INTO `tb_dict_region` (`id`, `name`, `pinyin`, `code`, `letter`, `parent_id`, `remarks`, `sort_no`) ");
            builder.append("VALUES (").append(startIdx).append(", ")
                    .append("'").append(it.getName()).append("', 'Pin Yin', ")
                    .append("'").append(it.getCode()).append("', 'PY', ").append(parentId).append(", ")
                    .append("'").append(it.getCode()).append("', ").append(i).append(".00);")
                    .append("\n");
            startIdx++;
        }

        logger.info("-------------- builder ------------------ \n");
        logger.info("\n" + builder);
        logger.info("-------------- builder ------------------ \n");
    }

    @Override
    public void generateSql(int startId, int targetNodeId, int fieldNum) {
        String prefix = "INSERT INTO `tb_plf_process_formprop_permis` (`id`, `node_id`, `property_id`, `see_able`, " +
                "`editable`, `required`, `create_time`, `update_time`) VALUES (";
        String suffix = ", 1, 0, 0, NOW(), NOW());";
        StringBuilder builder = new StringBuilder();
        int propertyId = 300;
        for (int i = 0; i < fieldNum; i++) {
            String str = startId + ", " + targetNodeId + ", " + propertyId;
            builder.append(prefix).append(str).append(suffix).append("\n");
            startId++;
            propertyId++;
        }
        logger.info("-------------- builder ------------------ \n");
        logger.info("\n" + builder);
        logger.info("-------------- builder ------------------ \n");
    }

}




