package com.lyloou.component.tool.execcommand;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.lyloou.component.cache.datacache.DataCache;
import com.lyloou.component.cache.datacache.DataCacheProperties;
import com.lyloou.component.cache.datacache.DataDefaultCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lilou
 * @since 2021/8/17
 */
@Configuration
@EnableConfigurationProperties({ExecCommandProperties.class})
public class ExecCommandAutoConfiguration {

    @Bean("execCommandSnowflake")
    Snowflake execCommandSnowflake(ExecCommandProperties properties) {
        return IdUtil.getSnowflake(properties.getWorkerId(), properties.getDatacenterId());
    }

    @Bean(name = "execCommandExecutor")
    public Executor execCommandExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("execCommandExecutor线程前缀-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(DataCache.class)
    public DataCache dataCache() {
        final DataCacheProperties dataCacheProperties = new DataCacheProperties();
        dataCacheProperties.setSchedulePrune(false);
        dataCacheProperties.setTimeout(-1);
        return new DataDefaultCache(dataCacheProperties);
    }

    @Bean
    @ConditionalOnMissingBean(value = ExecCommandController.class)
    public ExecCommandController execCommandController() {
        return new ExecCommandController();
    }
}
