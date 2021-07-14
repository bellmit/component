package com.lyloou.component.tool.swagger2;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lilou
 * @since 2021/7/14
 */
@Data
@ConfigurationProperties(prefix = "swagger2")
@Component
public class Swagger2Properties {
    /**
     * 是否开启swagger2
     */
    @Value("${enable:true}")
    private Boolean enable = true;

    /**
     * 扫描的包路径，可以不传，默认扫描 @Controller 和 @RestController
     */
    @Value("${basePackages:#{null}}")
    private List<String> basePackages;

    /**
     * HEADER中授权的KEY，默认 Authorization
     */
    @Value("${authorizationKey:Authorization}")
    private String authorizationKey;
}
