package com.lyloou.component.tool.execcommand;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lilou
 * @since 2021/8/17
 */
@Configuration
@EnableConfigurationProperties({ExecCommandProperties.class})
@EnableAsync
public class ExecCommandAutoConfiguration {

    @Bean("execCommandSnowflake")
    public Snowflake execCommandSnowflake(ExecCommandProperties properties) {
        return IdUtil.getSnowflake(properties.getWorkerId(), properties.getDatacenterId());
    }

    @Bean(name = "execCommandExecutor")
    public ThreadPoolTaskExecutor execCommandExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors() * 16);
        executor.setThreadNamePrefix("execCommandExecutor线程前缀-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(value = ExecCommandController.class)
    public ExecCommandController execCommandController() {
        return new ExecCommandController();
    }
}
