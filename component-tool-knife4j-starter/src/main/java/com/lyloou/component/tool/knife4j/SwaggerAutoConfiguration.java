package com.lyloou.component.tool.knife4j;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lilou
 * @since 2021/7/14
 */
@Configuration
@EnableConfigurationProperties({SwaggerGroupProperties.class})
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
@Slf4j
@Data
@ConditionalOnProperty(prefix = SwaggerGroupProperties.PREFIX, value = "enable", havingValue = "true", matchIfMissing = true)
public class SwaggerAutoConfiguration {
    private Map<String, Docket> docketItems = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        final SwaggerGroupProperties groupProperties = SpringUtil.getBean(SwaggerGroupProperties.class);
        if (!groupProperties.isEnable()) {
            return;
        }

        final Map<String, SwaggerItemProperties> items = groupProperties.getItems();
        if (CollectionUtil.isEmpty(items)) {
            return;
        }

        items.forEach(this::addDocket);
    }

    public void addDocket(String groupName, SwaggerItemProperties itemProperties) {
        if (!itemProperties.isEnable()) {
            return;
        }

        final Docket docket = DocketFactory.create(itemProperties);
        String key = "SWAGGER2_DOCKET_" + groupName;
        SpringUtil.registerBean(key, docket);
        docketItems.put(key, docket);
    }


    @Bean
    @ConditionalOnMissingBean(ModelPropertyBuilderPlugin.class)
    public ModelPropertyBuilderPlugin modelPropertyBuilderPlugin() {
        return new ApiModelPropertyBuilderPlugin();
    }


}
