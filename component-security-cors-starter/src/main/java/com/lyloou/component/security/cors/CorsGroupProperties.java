package com.lyloou.component.security.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ref: {@link org.springframework.web.cors.CorsConfiguration}
 *
 * @author lilou
 */
@Data
@ConfigurationProperties(prefix = "component.cors")
@Component
public class CorsGroupProperties {

    /**
     * 按组设置cors，默认全允许
     */
    private Map<String, CorsItemProperties> items = new LinkedHashMap<String, CorsItemProperties>() {{
        put("default", new CorsItemProperties());
    }};
}