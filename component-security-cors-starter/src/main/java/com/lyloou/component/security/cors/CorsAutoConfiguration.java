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
        return new FilterRegistrationBean<CorsFilter>(corsFilter) {{
            setOrder(0);
        }};
    }

    private CorsFilter createCorsFilter(CorsItemProperties corsProperties) {
        final String path = corsProperties.getPath();
        final CorsConfiguration corsConfiguration = new CorsConfiguration() {{
            setAllowCredentials(corsProperties.isAllowCredentials());

            for (String origin : corsProperties.getAllowedOrigins()) {
                addAllowedOrigin(origin);
            }

            for (String header : corsProperties.getAllowedHeaders()) {
                addAllowedHeader(header);
            }

            for (String method : corsProperties.getAllowedMethods()) {
                addAllowedMethod(method);
            }

            setMaxAge(corsProperties.getMaxAge());
        }};

        return new CorsFilter(new UrlBasedCorsConfigurationSource() {{
            registerCorsConfiguration(path, corsConfiguration);
        }});
    }

}