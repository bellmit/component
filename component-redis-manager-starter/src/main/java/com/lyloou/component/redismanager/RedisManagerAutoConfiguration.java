package com.lyloou.component.redismanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动注入定时器监听器配置
 *
 * @author lilou
 * @since 2021/3/7
 */
@EnableCaching
@Configuration
@Data
@EnableConfigurationProperties({RedisManagerProperties.class})
public class RedisManagerAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(RedisManagerApplicationContextHelper.class)
    public RedisManagerApplicationContextHelper redisManagerApplicationContextHelper() {
        return new RedisManagerApplicationContextHelper();
    }

    @Bean
    @ConditionalOnMissingBean(RedisService.class)
    RedisService redisService() {
        return new RedisServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(RedisManagerService.class)
    public RedisManagerService redisManagerHandler() {
        return new RedisManagerService();
    }

    @Bean
    @ConditionalOnMissingBean(RedisManagerController.class)
    public RedisManagerController redisManagerController() {
        return new RedisManagerController();
    }

    // /这里序列化解决@Cacheable 存值后value 显示字节码问题
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager(RedisManagerProperties redisManagerProperties, RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultConfig = getDefaultRedisCacheConfiguration(redisManagerProperties);

        if (!redisManagerProperties.getCacheNullValues()) {
            // allow cache null values
            defaultConfig = defaultConfig.disableCachingNullValues();
        }

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(getCacheConfigurations(redisManagerProperties))
                .build();
    }

    private RedisCacheConfiguration getDefaultRedisCacheConfiguration(RedisManagerProperties redisManagerProperties) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();
        return RedisCacheConfiguration.defaultCacheConfig()
                //失效时间
                .entryTtl(Duration.ofSeconds(redisManagerProperties.getTtl()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                // 配置序列化（解决乱码的问题）
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
    }

    public Map<String, RedisCacheConfiguration> getCacheConfigurations(RedisManagerProperties redisManagerProperties) {
        final HashMap<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        for (CacheConfig cacheConfig : redisManagerProperties.getCacheConfigList()) {
            if (cacheConfig.getTtl() == null) {
                cacheConfig.setTtl(redisManagerProperties.getTtl());
            }
            if (cacheConfig.getCacheNullValues() == null) {
                cacheConfig.setCacheNullValues(redisManagerProperties.getCacheNullValues());
            }

            RedisCacheConfiguration defaultConfig = getDefaultRedisCacheConfiguration(redisManagerProperties);
            defaultConfig = defaultConfig.entryTtl(Duration.ofSeconds(cacheConfig.getTtl()));
            if (!cacheConfig.getCacheNullValues()) {
                defaultConfig = defaultConfig.disableCachingNullValues();
            }
            cacheConfigurations.put(cacheConfig.getCacheName(), defaultConfig);
        }
        return cacheConfigurations;
    }

    @Bean
    @ConditionalOnMissingBean(Jackson2JsonRedisSerializer.class)
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false)
                .configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    //这里序列化解决手动存值后value 显示字节码问题
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        //配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(factory);

        //设置序列化
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        //使用Jackson2JsonRedisSerializer<Object>来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();
        //key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        //value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        //Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

