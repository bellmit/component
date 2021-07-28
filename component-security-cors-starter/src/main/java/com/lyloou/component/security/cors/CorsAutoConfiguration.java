package com.lyloou.component.security.cors;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({CorsGroupProperties.class})
@Slf4j
@Data
public class CorsAutoConfiguration {
    private Map<String, CorsFilter> corsFilterMap = new LinkedHashMap<>();

    @PostConstruct
    public void corsFilter() {
        final CorsGroupProperties corsGroupProperties = SpringUtil.getBean(CorsGroupProperties.class);
        final Map<String, CorsItemProperties> items = corsGroupProperties.getItems();
        items.forEach((groupName, corsItemProperties) -> {
            final CorsFilter corsFilter = createCorsFilter(corsItemProperties);
            final String beanName = "CORS_FILTER_" + groupName;
            corsFilterMap.put(beanName, corsFilter);

            SpringUtil.registerBean(beanName, new FilterRegistrationBean<CorsFilter>(corsFilter) {{
                setOrder(0);
            }});
        });

    }

    public CorsFilter createCorsFilter(CorsItemProperties corsProperties) {
        return new CorsFilter(new UrlBasedCorsConfigurationSource() {{
            registerCorsConfiguration(corsProperties.getPath(), new CorsConfiguration() {{
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
            }});
        }});
    }
}