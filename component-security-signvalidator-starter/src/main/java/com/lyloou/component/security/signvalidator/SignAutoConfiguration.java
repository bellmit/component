package com.lyloou.component.security.signvalidator;

import com.lyloou.component.security.signvalidator.cache.DataCache;
import com.lyloou.component.security.signvalidator.cache.DataDefaultCache;
import com.lyloou.component.security.signvalidator.constant.SignConstant;
import com.lyloou.component.security.signvalidator.filter.RequestWrapperFilter;
import com.lyloou.component.security.signvalidator.interceptor.SignInterceptor;
import com.lyloou.component.security.signvalidator.properties.SignProperties;
import com.lyloou.component.security.signvalidator.validator.DefaultSignValidator;
import com.lyloou.component.security.signvalidator.validator.RemoteSignValidator;
import com.lyloou.component.security.signvalidator.validator.SignValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lilou
 * @since 2021/7/19
 */
@Configuration
@EnableConfigurationProperties({SignProperties.class})
@Slf4j
public class SignAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignInterceptor());
    }

    @Bean
    public FilterRegistrationBean<RequestWrapperFilter> signFilter() {
        FilterRegistrationBean<RequestWrapperFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Bean(SignConstant.DEFAULT_SIGN_VALIDATOR)
    public SignValidator defaultSignValidator(SignProperties signProperties) {
        return new DefaultSignValidator(signProperties);
    }

    @Bean(SignConstant.REMOTE_SIGN_VALIDATOR)
    public SignValidator remoteSignValidator(SignProperties signProperties) {
        return new RemoteSignValidator(signProperties);
    }

    @Bean
    @ConditionalOnMissingBean(DataCache.class)
    public DataCache dataCache(SignProperties signProperties) {
        return new DataDefaultCache(signProperties);
    }

}
