package com.lyloou.component.loggerrequeststatistic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lilou
 */
@EnableAspectJAutoProxy
@Configuration
@Slf4j
public class RequestStatisticAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestStatisticInterceptor());
    }

    @Bean
    public FilterRegistrationBean<RequestWrapperFilter> loggingFilter() {
        FilterRegistrationBean<RequestWrapperFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

}