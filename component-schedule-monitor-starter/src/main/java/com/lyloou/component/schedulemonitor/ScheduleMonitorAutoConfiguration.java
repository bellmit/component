package com.lyloou.component.schedulemonitor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 自动注入定时器监听器配置
 *
 * @author lilou
 * @since 2021/3/7
 */
@Configuration
@EnableAspectJAutoProxy
public class ScheduleMonitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ScheduleMonitorAspect.class)
    public ScheduleMonitorAspect scheduleMonitorAspect() {
        return new ScheduleMonitorAspect();
    }

    @Bean
    @ConditionalOnMissingBean(ScheduleMonitorApplicationContextHelper.class)
    public ScheduleMonitorApplicationContextHelper scheduleMonitorApplicationContextHelper() {
        return new ScheduleMonitorApplicationContextHelper();
    }

    @Bean
    @ConditionalOnMissingBean(ScheduleMonitorHandler.class)
    public ScheduleMonitorHandler scheduleMonitorHandler() {
        return new ScheduleMonitorHandler();
    }

    @Bean
    @ConditionalOnMissingBean(ScheduleMonitorController.class)
    public ScheduleMonitorController scheduleMonitorController() {
        return new ScheduleMonitorController();
    }

}
