package com.lyloou.component.exceptionhandler;

import com.lyloou.component.exceptionhandler.handler.CommonExceptionHandler;
import com.lyloou.component.exceptionhandler.handler.ScheduleTaskErrorHandler;
import com.lyloou.component.exceptionhandler.service.ExceptionHandlerServiceLogImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lilou
 * @since 2021/4/29
 */
@Configuration
@EnableAspectJAutoProxy
public class ExceptionHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ExceptionHandlerServiceLogImpl.class)
    public ExceptionHandlerServiceLogImpl exceptionHandlerServiceLogImpl() {
        return new ExceptionHandlerServiceLogImpl();
    }

    @Bean
    @ConditionalOnMissingBean(CommonExceptionHandler.class)
    public CommonExceptionHandler commonExceptionHandler() {
        return new CommonExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(ScheduleTaskErrorHandler.class)
    public ScheduleTaskErrorHandler scheduleTaskErrorHandler() {
        return new ScheduleTaskErrorHandler();
    }

}
