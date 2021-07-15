package com.lyloou.component.config.keyvalue;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lilou
 * @since 2021/7/15
 */
@Configuration
@EnableConfigurationProperties(KeyValueProperties.class)
public class KeyValueAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = KeyValueController.class)
    public KeyValueController keyValueController() {
        return new KeyValueController();
    }
}
