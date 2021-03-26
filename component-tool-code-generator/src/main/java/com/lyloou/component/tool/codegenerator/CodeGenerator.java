package com.lyloou.component.tool.codegenerator;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

// [【DB系列】Mybatis-Plus代码自动生成 | 一灰灰Blog](https://spring.hhui.top/spring-blog/2020/04/06/200406-SpringBoot%E7%B3%BB%E5%88%97%E6%95%99%E7%A8%8B%E4%B9%8BMybatis-Plus%E4%BB%A3%E7%A0%81%E8%87%AA%E5%8A%A8%E7%94%9F%E6%88%90/)
// https://mp.baomidou.com/guide/generator.html
public class CodeGenerator {

    private static final String SQL_URL = "jdbc:mysql://127.0.0.1:3306/mybatisplus?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private static final String SQL_USERNAME = "root";
    private static final String SQL_PASSWORD = "root";
    private static final String SQL_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    private String sqlUrl;
    private String sqlUsername;
    private String sqlPassword;
    private String sqlDriverName;
    private String[] strategyTableArray;
    private String strategyTablePrefix;
    private String packageEntity;
    private String packagePath;
    private String projectPath;
    private String projectAuthor;
    private String enableSwagger;

    private String strategySuperEntityClass;
    private String strategySuperServiceClass;
    private String strategySuperControllerClass;
    private String strategySuperEntityColumns;

    public static void main(String[] args) throws IOException {
        new CodeGenerator().start();
    }

    public void start() throws IOException {
        loadProperties();
        newAutoGenerator().execute();
    }

    private void loadProperties() throws IOException {
        InputStream inputStream = CodeGenerator.class.getClassLoader().getResourceAsStream("code-generator.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        sqlUrl = properties.getProperty("sql.url", SQL_URL);
        sqlUsername = properties.getProperty("sql.username", SQL_USERNAME);
        sqlPassword = properties.getProperty("sql.password", SQL_PASSWORD);
        sqlDriverName = properties.getProperty("sql.driver_name", SQL_DRIVER_NAME);


        strategyTablePrefix = properties.getProperty("sql.table.prefix", "");
        String sqlTableArray = properties.getProperty("sql.table.array", "");
        strategyTableArray = sqlTableArray.split(",");
        for (int i = 0; i < strategyTableArray.length; i++) {
            strategyTableArray[i] = strategyTableArray[i].trim();
        }

        packageEntity = properties.getProperty("package.entity", "entity");
        packagePath = properties.getProperty("package.path", "com.lyloou");
        projectPath = properties.getProperty("sys.project.path", "/tmp/");
        projectAuthor = properties.getProperty("sys.project.author", "lyloou");
        enableSwagger = properties.getProperty("sys.enableSwagger", "false");

        strategySuperEntityClass = properties.getProperty("strategy.super.entity.class", null);
        strategySuperEntityColumns = properties.getProperty("strategy.super.entity.columns", null);
        strategySuperServiceClass = properties.getProperty("strategy.super.service.class", null);
        strategySuperControllerClass = properties.getProperty("strategy.super.controller.class", null);
    }

    // 代码生成器
    public AutoGenerator newAutoGenerator() {
        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(getGlobalConfig());
        generator.setDataSource(getDataSourceConfig());

        generator.setPackageInfo(getPackageConfig());
        // generator.setCfg(getInjectionConfig(getPackageConfig()));
        generator.setTemplate(getTemplateConfig());
        generator.setStrategy(getStrategyConfig());
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        return generator;
    }

    private String getMainDir() {
        return projectPath + "/src/main";
    }

    // 全局配置
    private GlobalConfig getGlobalConfig() {
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(getMainDir() + "/java");
        gc.setAuthor(projectAuthor);
        gc.setOpen(false);
        // 覆盖写
        gc.setFileOverride(true);

        gc.setBaseResultMap(true);
        gc.setEntityName("%sEntity");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setSwagger2(Boolean.parseBoolean(enableSwagger));

        // gc.setBaseColumnList(true);
        return gc;
    }

    // 数据源配置
    private DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(sqlUrl);
        // dsc.setSchemaName("public");
        dsc.setDriverName(sqlDriverName);
        dsc.setUsername(sqlUsername);
        dsc.setPassword(sqlPassword);

        // 类型转换
        dsc.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                String t = fieldType.toLowerCase();
                if (Stream.of("timestamp", "datetime").anyMatch(tt -> tt.equalsIgnoreCase(t))) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });
        return dsc;
    }

    // 包配置
    private PackageConfig getPackageConfig() {
        PackageConfig pc = new PackageConfig();
        // 不额外指定模块，如果指定为 test，则生成的xml会在 mapper/test/ 目录下
        pc.setModuleName("");
        pc.setEntity(packageEntity);
        pc.setParent(packagePath);
        return pc;
    }

    // 自定义配置
    private InjectionConfig getInjectionConfig(PackageConfig pc) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return getMainDir() + "/resources/mapper/"
                        + pc.getModuleName() + "/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    // 配置模板
    private TemplateConfig getTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        // templateConfig.setService("templates/service.java");
        // templateConfig.setServiceImpl(null);
        // 不自动生成controller类
        templateConfig.setController(null);
        return templateConfig;
    }

    // 策略配置
    private StrategyConfig getStrategyConfig() {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);

        // 公共父类
        strategy.setRestControllerStyle(true);
        strategy.setSuperEntityClass(strategySuperEntityClass);
        strategy.setSuperServiceClass(strategySuperServiceClass);
        strategy.setSuperControllerClass(strategySuperControllerClass);
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns(strategySuperEntityColumns.split(","));

        // 设置需要生成的表名
        strategy.setInclude(strategyTableArray);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(strategyTablePrefix);
        return strategy;
    }

}