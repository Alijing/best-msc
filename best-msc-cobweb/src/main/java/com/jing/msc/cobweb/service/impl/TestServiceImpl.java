package com.jing.msc.cobweb.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jing.common.core.enums.ResultEnum;
import com.jing.common.core.exception.CustomException;
import com.jing.msc.cobweb.entity.test.ResGroup;
import com.jing.msc.cobweb.listener.ResGroupReadListener;
import com.jing.msc.cobweb.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author : jing
 * @since : 2024/12/10 11:32
 */
@Service(value = "testService")
public class TestServiceImpl implements TestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @Override
    public List<String> getJsonKeys(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        List<String> keys = new ArrayList<>();
        StringJoiner joiner = new StringJoiner("\n");
        extractKeys(jsonObject, keys, joiner);
        logger.info("keys : {}", joiner);
        return new ArrayList<>(keys);
    }

    private static void extractKeys(JSONObject jsonObject, List<String> keys, StringJoiner joiner) {
        for (String key : jsonObject.keySet()) {
            keys.add(key);
            joiner.add(key);
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                extractKeys((JSONObject) value, keys, joiner);
            } else if (value instanceof JSONArray) {
                for (Object item : (JSONArray) value) {
                    if (item instanceof JSONObject) {
                        extractKeys((JSONObject) item, keys, joiner);
                    }
                }
            }
        }
    }

}
