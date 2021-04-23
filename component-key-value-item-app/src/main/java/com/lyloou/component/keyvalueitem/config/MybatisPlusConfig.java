package com.lyloou.component.keyvalueitem.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlusConfig
 *
 * @author liuchang
 **/
@Configuration
@MapperScan("com.lyloou.component.keyvalueitem.repository.mapper")
public class MybatisPlusConfig {
    @Bean
    public MetaObjectHandler mybatisObjectHandler() {
        return new MybatisObjectHandler();
    }


}
