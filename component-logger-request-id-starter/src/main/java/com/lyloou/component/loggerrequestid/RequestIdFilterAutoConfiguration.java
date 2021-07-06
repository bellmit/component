package com.lyloou.component.loggerrequestid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestIdFilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RequestIdFilter.class)
    RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

}
