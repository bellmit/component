package com.lyloou.component.redismanager;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
# yml 配置示例
spring:
  redis:
    host: 127.0.0.1
    password: ''
    port: 6379
    cache-null-values: true
    ttl: 611
    cache-config-list:
      - cache-name: com.lyloou.component.redismanager.user
        cache-null-values: false
        ttl: 888
      - cache-name: com.lyloou.component.redismanager.person
        cache-null-values: true
        ttl: 998
 */

/**
 * @author lilou
 * @since 2021/4/7
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
@Component
public class RedisManagerProperties {
    /**
     * 是否缓存null，默认缓存
     */
    @Value("${cache-null-values:true}")
    private Boolean cacheNullValues = true;

    /**
     * 超时时间，默认5分钟
     */
    @Value("${ttl:300}")
    private Integer ttl = 300;

    /**
     * 为某些key单独设置ttl和cacheNullValues
     * [玩转Spring Cache --- 扩展缓存注解支持失效时间TTL【享学Spring】_YourBatman-CSDN博客](https://blog.csdn.net/f641385712/article/details/95234347)
     * [spring boot 的yml配置文件定义list集合、数组和map以及使用中出现的错误_冰冻非一日之寒-CSDN博客_yml配置list集合](https://blog.csdn.net/you18131371836/article/details/104839901)
     */
    private List<CacheConfig> cacheConfigList = new ArrayList<>();
}
