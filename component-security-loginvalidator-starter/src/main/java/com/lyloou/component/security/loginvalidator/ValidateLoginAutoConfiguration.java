package com.lyloou.component.security.loginvalidator;

import com.lyloou.component.cache.datacache.DataCache;
import com.lyloou.component.cache.datacache.DataCacheProperties;
import com.lyloou.component.cache.datacache.DataDefaultCache;
import com.lyloou.component.security.loginvalidator.aspect.ValidateLoginAspect;
import com.lyloou.component.security.loginvalidator.properties.TokenProperties;
import com.lyloou.component.security.loginvalidator.service.TokenService;
import com.lyloou.component.security.loginvalidator.service.TokenServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lilou
 * @since 2021/7/19
 */
@Configuration
@EnableConfigurationProperties({TokenProperties.class})
@Import({ValidateLoginAspect.class})
public class ValidateLoginAutoConfiguration {


    /**
     * 默认使用内存缓存，基于ConcurrentHashMap实现
     *
     * @param tokenProperties 配置
     * @return 缓存对象
     */
    @Bean
    @ConditionalOnMissingBean(DataCache.class)
    public DataCache dataCache(TokenProperties tokenProperties) {
        final DataCacheProperties cacheProperties = new DataCacheProperties();
        cacheProperties.setSchedulePrune(true);
        cacheProperties.setTimeout(tokenProperties.getExpireSecond());
        return new DataDefaultCache(cacheProperties);
    }

    @Bean
    @ConditionalOnMissingBean(TokenService.class)
    public TokenService tokenService() {
        return new TokenServiceImpl();
    }

}
