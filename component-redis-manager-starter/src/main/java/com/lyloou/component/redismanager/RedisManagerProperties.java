package com.lyloou.component.redismanager;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lilou
 * @since 2021/4/7
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisManagerProperties {
    /**
     * 是否缓存null，默认缓存
     */
    @Value("${cache-null-values:true}")
    private Boolean cacheNullValues = true;

    /**
     * 超时时间，默认5分钟
     */
    @Value("${expire-ttl:300}")
    private Integer expireTtl = 300;
}
