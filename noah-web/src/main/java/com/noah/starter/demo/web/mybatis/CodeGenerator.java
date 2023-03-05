package com.noah.starter.demo.web.mybatis;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.sql.SQLException;
import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) throws SQLException {

        String projectPath = new File(System.getProperty("user.dir")).getParent() + "/noah-starter/noah-web/src/main/java";
        String resourePath = new File(System.getProperty("user.dir")).getParent() + "/noah-starter/noah-web/src/main/";

        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/noah?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai&useAffectedRows=true",
                        "root", "Noah123@")
                .globalConfig(builder -> {
                    builder.fileOverride() // 覆盖已生成文件
                            .outputDir(projectPath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.noah.starter.demo.web.mybatis") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, resourePath + "/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("product"); // 设置需要生成的表名

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }

}
