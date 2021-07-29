package com.lyloou.component.security.cors;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lilou
 */
@Configuration
@EnableConfigurationProperties({CorsGroupProperties.class})
@Slf4j
@Data
public class CorsAutoConfiguration implements BeanFactoryPostProcessor {
    private Map<String, CorsFilter> corsFilterMap = new LinkedHashMap<>();

    public CorsFilter createCorsFilter(CorsItemProperties corsProperties) {
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

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        final CorsGroupProperties corsGroupProperties = configurableListableBeanFactory.getBean(CorsGroupProperties.class);
        final Map<String, CorsItemProperties> items = corsGroupProperties.getItems();
        items.forEach((groupName, corsItemProperties) -> {
            final String beanName = "CORS_FILTER_" + groupName;
            final CorsFilter corsFilter = createCorsFilter(corsItemProperties);
            corsFilterMap.put(beanName, corsFilter);

            configurableListableBeanFactory.registerSingleton(beanName, new FilterRegistrationBean<CorsFilter>(corsFilter) {{
                setOrder(0);
            }});
        });
    }
}