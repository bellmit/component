package com.lyloou.component.security.loginvalidator;

import com.lyloou.component.security.loginvalidator.aspect.ValidateLoginAspect;
import com.lyloou.component.security.loginvalidator.cache.DataCache;
import com.lyloou.component.security.loginvalidator.cache.DataDefaultCache;
import com.lyloou.component.security.loginvalidator.properties.TokenProperties;
import com.lyloou.component.security.loginvalidator.service.TokenService;
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


    @Bean
    @ConditionalOnMissingBean(DataCache.class)
    public DataCache dataCache(TokenProperties signProperties) {
        return new DataDefaultCache(signProperties);
    }

    @Bean
    @ConditionalOnMissingBean(TokenService.class)
    public TokenService tokenService() {
        return new TokenService();
    }

}
