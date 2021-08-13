package com.lyloou.component.loggercontroller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动注入定时器监听器配置
 *
 * @author lilou
 * @since 2021/3/7
 */
@Configuration
public class LoggerControllerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(LogLevelController.class)
    public LogLevelController logLevelController() {
        return new LogLevelController();
    }

}
