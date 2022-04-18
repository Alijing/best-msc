package com.jing.common.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.ArrayList;
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
        String url = "jdbc:mysql://localhost:3306/jg_om_dev?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8";
        String userName = "root";
        String password = "Fullsee@123";
        String moduleName = "jiaogaunom";
        String projectPath = System.getProperty("user.dir") + "/best-msc-common/best-msc-generator";
        String outputPath = projectPath + "/src/main/java";
        String xmlPath = projectPath + "/src/main/resources/mapper/" + moduleName;
        FastAutoGenerator.create(url, userName, password)
                .globalConfig(builder -> {
                    builder.author("jing")
                            .enableSwagger()
                            .fileOverride()
                            .outputDir(outputPath)
                            .dateType(DateType.ONLY_DATE)
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    // 设置父包名
                    builder.parent("com.jing.msc")
                            // 设置父包模块名
                            .moduleName(moduleName)
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlPath));
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude("TB_JG_OM_ASPECT")
                            // 设置过滤表前缀
                            .addTablePrefix("TB_JG_OM_", "c_")
                            .entityBuilder()
                            .naming(NamingStrategy.no_change)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .enableTableFieldAnnotation()
                            .logicDeleteColumnName("logic_del")
                            .logicDeletePropertyName("logicDel")
                            .addTableFills(tableFills())
                            .serviceBuilder()
                            .formatServiceFileName("%sService");
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }


    private static List<IFill> tableFills() {
        List<IFill> fills = new ArrayList<>();
        fills.add(new Column("create_time", FieldFill.INSERT));
        fills.add(new Column("create_by", FieldFill.INSERT));
        fills.add(new Column("update_by", FieldFill.INSERT_UPDATE));
        fills.add(new Column("update_time", FieldFill.INSERT_UPDATE));
        return fills;
    }

}
