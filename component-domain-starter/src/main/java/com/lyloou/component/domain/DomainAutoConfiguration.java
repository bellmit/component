package com.lyloou.component.domain;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DomainApplicationContextHelper.class)
    public DomainApplicationContextHelper applicationContextHelper() {
        return new DomainApplicationContextHelper();
    }
}
