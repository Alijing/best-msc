package com.jing.common.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.generator
 * @date : 2021/4/26 10:36
 * @description :
 */
public class CodeGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/dev_jing_msc?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8";
        String userName = "root";
        String password = "Fullsee@123";

        // 设置父包名
        String pkg = "com.fullsee.integratedbis";
        // 设置父包模块名
        String moduleName = "";

        String projectPath = System.getProperty("user.dir") + "/best-msc-common/best-msc-generator";
        String outputPath = projectPath + "/src/main/java";

        // 设置mapperXml生成路径
        String xmlPath = projectPath + "/src/main/resources/mapper/" + moduleName;
        FastAutoGenerator.create(url, userName, password)
                .globalConfig(builder ->
                        builder.author("jing")
                                .enableSwagger()
                                .outputDir(outputPath)
                                .dateType(DateType.ONLY_DATE)
                                .disableOpenDir()
                                .commentDate("yyyy-MM-dd HH:mm:ss")
                )
                .packageConfig(builder ->
                        builder.parent(pkg)
                                .moduleName(moduleName)
                                .pathInfo(Collections.singletonMap(OutputFile.xml, xmlPath))
                )
                .strategyConfig(builder ->
                        builder.addInclude(includes())
                                .addTablePrefix(tablePrefix())
                                .entityBuilder()
                                .naming(NamingStrategy.underline_to_camel)
                                .columnNaming(NamingStrategy.underline_to_camel)
                                .enableTableFieldAnnotation()
                                .logicDeleteColumnName("logic_del")
                                .logicDeletePropertyName("logicDel")
                                .addTableFills(tableFills())
                                .idType(IdType.ASSIGN_ID)
                                .enableRemoveIsPrefix()
                                .fileOverride()
                                .serviceBuilder()
                                .formatServiceFileName("%sService")
                )
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }

    /**
     * 设置需要生成的表名
     *
     * @return 需要生成的表名
     * @author jing
     * @date 2022/4/19 10:40
     */
    private static List<String> includes() {
        return Arrays.asList("sys_department", "TB_COMMON_DICT_ITEM","TB_PSMA_GOODS");
    }


    /**
     * 设置过滤表前缀
     *
     * @return 表前缀
     * @author jing
     * @date 2022/4/19 10:40
     */
    private static List<String> tablePrefix() {
        return Arrays.asList("tb_common_", "TB_SYSTEM_", "tb_cems_", "tb_basic_", "sys_", "tb_vioms_", "tb_psma_");
    }

    private static List<IFill> tableFills() {
        List<IFill> fills = new ArrayList<>();
        fills.add(new Column("create_time", FieldFill.INSERT));
        fills.add(new Column("create_by", FieldFill.INSERT));
        fills.add(new Column("creator", FieldFill.INSERT));
        fills.add(new Column("update_by", FieldFill.INSERT_UPDATE));
        fills.add(new Column("updater", FieldFill.INSERT_UPDATE));
        fills.add(new Column("update_time", FieldFill.INSERT_UPDATE));
        return fills;
    }

}
