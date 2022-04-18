package com.jing.msc.cobweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.cobweb.dao.SpiderMapper;
import com.jing.msc.cobweb.entity.Spider;
import com.jing.msc.cobweb.service.SpiderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
    public void generate(Integer step) {
        String destFilePath = System.getProperty("user.dir");
        if (1 == step) {
            generateOne(destFilePath + File.separator + "filed.sql");
        } else if (2 == step) {
            generateTwo(destFilePath + File.separator + "filed.sql", destFilePath + File.separator + "update.sql");
        }
    }

    private void generateTwo(String oriFilePath, String destFilePath) {
        String oriTableSchema = "ivms_320_24";
        String destTableSchema = "ivms_320";
        File file = new File(oriFilePath);
        BufferedReader reader = null;
        List<Map<String, List<String>>> destTableFiled = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(tempString)) {
                    String[] split = tempString.split(":");
                    List<String> collect = Arrays.stream(split[1].split(",")).collect(Collectors.toList());
                    Map<String, List<String>> map = new HashMap<>();
                    map.put(split[0], collect);
                    destTableFiled.add(map);
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            logger.error("读取 SQL 失败 : ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    logger.error("读取 SQL 失败 : ", e1);
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("SET FOREIGN_KEY_CHECKS = 0;").append("\n").append("\n");
        for (Map<String, List<String>> it : destTableFiled) {
            Set<String> keySet = it.keySet();
            List<Map<String, String>> oriTableFileds = new ArrayList<>();
            List<String> destFiled = new ArrayList<>();
            String tableName = null;
            for (String key : keySet) {
                tableName = key;
                oriTableFileds = selectAllFiled(tableName);
                destFiled = it.get(tableName);
            }
            if (StringUtils.isBlank(tableName) || null == oriTableFileds || oriTableFileds.size() < 1) {
                continue;
            }

            builder.append("REPLACE INTO `").append(destTableSchema).append("`.`").append(tableName).append("` ( ").append("\n");
            for (int i = 0; i < destFiled.size(); i++) {
                String filed = destFiled.get(i);
                builder.append(filed);
                if (i != destFiled.size() - 1) {
                    builder.append(", ");
                }
            }
            builder.append("\n").append(") ");
            builder.append("( SELECT  ");
            for (int i = 0; i < destFiled.size(); i++) {
                String field = destFiled.get(i);
                String tempField = null;
                for (Map<String, String> oriTableFiled : oriTableFileds) {
                    String oriField = oriTableFiled.get("Field");
                    if (field.trim().equalsIgnoreCase("`" + oriField + "`")) {
                        tempField = oriField;
                        break;
                    }
                }
                if (StringUtils.isBlank(tempField)) {
                    builder.append(" NULL ");
                } else {
                    builder.append("`").append(tempField).append("`");
                }
                if (i != destFiled.size() - 1) {
                    builder.append(", ");
                }

            }

            builder.append("FROM `").append(oriTableSchema).append("`.`").append(tableName).append("`);");
            builder.append("\n").append("\n").append("\n");
        }
        builder.append("SET FOREIGN_KEY_CHECKS = 1;").append("\n").append("\n");
        File destFile = new File(destFilePath);
        if (!destFile.exists()) {
            try {
                boolean newFile = destFile.createNewFile();
            } catch (IOException e) {
                logger.error("文件创建失败 : ", e);
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(destFile);
            fw.write(builder.toString());
        } catch (Exception ex) {
            logger.error("SQL 写入文件失败 : ", ex);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void generateOne(String destFilePath) {
        String tableSchema = "ivms_320";
        List<Map<String, Object>> tables = selectAllTable(tableSchema);
        String[] excludeTables = new String[]{
                "sys_user", "ChannelTbl", "sys_role", "ResGroupTbl", "ResGroupBindTbl", "RolePriviTbl",
                "RoleResGroupBindTbl", "DomainChannelBindTbl",
        };
        for (String table : excludeTables) {
            int idx = -1;
            for (int i = 0; i < tables.size(); i++) {
                String tableName = tables.get(i).get("tableName").toString();
                if (table.equalsIgnoreCase(tableName)) {
                    idx = i;
                    break;
                }
            }
            if (-1 != idx) {
                tables.remove(idx);
            }
        }

        StringBuilder builder = new StringBuilder();
        for (Map<String, Object> map : tables) {
            String tableName = map.get("tableName").toString();
            List<Map<String, String>> filed = selectAllFiled(tableName);
            builder.append(tableName).append(":");
            for (int i = 0; i < filed.size(); i++) {
                String fieldName = filed.get(i).get("Field");
                builder.append("`").append(fieldName).append("`");
                if (i != filed.size() - 1) {
                    builder.append(", ");
                }
            }
            builder.append("\n").append("\n").append("\n");
        }

        File destFile = new File(destFilePath);
        if (!destFile.exists()) {
            try {
                boolean newFile = destFile.createNewFile();
            } catch (IOException e) {
                logger.error("文件创建失败 : ", e);
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(destFile);
            fw.write(builder.toString());
        } catch (Exception ex) {
            logger.error("SQL 写入文件失败 : ", ex);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> selectAllTable(String tableSchema) {
        return baseMapper.selectAllTable(tableSchema);
    }

    @Override
    public List<Map<String, String>> selectAllFiled(String tableName) {
        try {
            return baseMapper.selectAllFiled(tableName);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}




