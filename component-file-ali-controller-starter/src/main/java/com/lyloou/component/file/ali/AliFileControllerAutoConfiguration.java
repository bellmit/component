package com.lyloou.component.file.ali;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lilou
 * @since 2021/4/29
 */
@Configuration
@EnableConfigurationProperties({AliFileProperties.class})
public class AliFileControllerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(AliFileController.class)
    public AliFileController aliController() {
        return new AliFileController();
    }

    @Bean
    @ConditionalOnMissingBean(AliFileService.class)
    public AliFileService aliService() {
        return new AliFileServiceImpl();
    }

}
