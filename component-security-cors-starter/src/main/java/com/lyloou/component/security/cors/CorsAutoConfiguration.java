package com.lyloou.component.security.cors;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author lilou
 */
@Configuration
@EnableConfigurationProperties({CorsItemProperties.class})
@Slf4j
@Data
public class CorsAutoConfiguration {

    @Bean("corsFilter")
    public FilterRegistrationBean<CorsFilter> corsFilter(CorsItemProperties corsItemProperties) {
        final CorsFilter corsFilter = createCorsFilter(corsItemProperties);
        final FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<CorsFilter>(corsFilter);
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    private CorsFilter createCorsFilter(CorsItemProperties corsProperties) {

        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(corsProperties.isAllowCredentials());
        for (String origin : corsProperties.getAllowedOrigins()) {
            corsConfiguration.addAllowedOrigin(origin);
        }
        for (String header : corsProperties.getAllowedHeaders()) {
            corsConfiguration.addAllowedHeader(header);
        }
        for (String method : corsProperties.getAllowedMethods()) {
            corsConfiguration.addAllowedMethod(method);
        }
        corsConfiguration.setMaxAge(corsProperties.getMaxAge());

        final UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration(corsProperties.getPath(), corsConfiguration);
        return new CorsFilter(configSource);
    }

}