package com.lyloou.component.tool.swagger2;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author lilou
 * @since 2021/7/14
 */
@Configuration
@EnableConfigurationProperties({SwaggerItemProperties.class})
@EnableSwagger2WebMvc
public class Swagger2AutoConfiguration {
    @Bean
    public Docket swagger2Docket(SwaggerItemProperties swagger2Properties) {
        return DocketFactory.create(swagger2Properties);
    }
}
