package com.lyloou.component.security.signvalidator.properties;

import com.lyloou.component.security.signvalidator.cache.DataCacheProperties;
import com.lyloou.component.security.signvalidator.constant.SignConstant;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签名相关的总配置类
 *
 * @author lilou
 * @since 2021/7/19
 */
@Data
@ConfigurationProperties(prefix = "component.sign")
@Component
public class SignProperties {
    /**
     * 全局开关：是否启用 签名
     */
    @Value("${enabled:true}")
    private Boolean enabled = true;

    /**
     * true会忽略验证，只有被注解了{@link com.lyloou.component.security.signvalidator.annotation.ValidateSign}才会验证；false会全局验证，只有被注解了{@link com.lyloou.component.security.signvalidator.annotation.IgnoreValidateSign}才不会验证
     */
    private Boolean ignoreValidate = true;

    /**
     * 验证器，可以配置多个 validatorName -> validatorConfig（默认：default）
     */
    private Map<String, ValidatorConfig> validators = new HashMap<String, ValidatorConfig>() {{
        // 默认的验证器
        put(SignConstant.DEFAULT_VALIDATOR_NAME, new ValidatorConfig());
    }};

    /**
     * 缓存配置类
     */
    @NestedConfigurationProperty
    private DataCacheProperties cache = new DataCacheProperties();

    /**
     * 签名客户端列表。（需要动态增删的话，可以修改此列表）
     */
    private List<ClientConfig> clients = new ArrayList<>();
}
