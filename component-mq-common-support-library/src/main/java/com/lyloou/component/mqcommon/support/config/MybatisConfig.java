package com.lyloou.component.mqcommon.support.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lilou
 * @since 2021/8/11
 */
@Configuration
@MapperScan("com.lyloou.component.mqcommon.support.mapper")
public class MybatisConfig {
}
