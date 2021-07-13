package com.lyloou.tool.captcha;

import com.lyloou.tool.captcha.cache.CodeCache;
import com.lyloou.tool.captcha.cache.CodeDefaultCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lilou
 */
@Configuration
@Slf4j
public class CaptchaAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(captchaInterceptor());
    }


    @Bean
    @ConditionalOnMissingBean(CaptchaInterceptor.class)
    public CaptchaInterceptor captchaInterceptor() {
        return new CaptchaInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(CaptchaController.class)
    public CaptchaController controller() {
        return new CaptchaController();
    }

    @Bean
    @ConditionalOnMissingBean(CodeCache.class)
    public CodeCache codeCache() {
        return CodeDefaultCache.INSTANCE;
    }

    @Bean
    @ConditionalOnMissingBean(CaptchaService.class)
    public CaptchaService captchaService() {
        return new CaptchaServiceImpl();
    }
}