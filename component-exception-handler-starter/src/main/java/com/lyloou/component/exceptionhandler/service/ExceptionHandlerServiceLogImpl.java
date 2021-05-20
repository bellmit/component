package com.lyloou.component.exceptionhandler.service;

import com.lyloou.component.exceptionhandler.handler.CommonExceptionHandler;
import com.lyloou.component.exceptionhandler.model.ErrorLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p>用日志来记录异常</p>
 *
 * @author lyloou
 * @since 2020/12/22
 */
@Component
@Slf4j
public class ExceptionHandlerServiceLogImpl implements ExceptionHandlerService {

    @Value("${spring.application.name:app}")
    String appName;

    @Autowired
    CommonExceptionHandler globalExceptionHandler;

    @PostConstruct
    @Override
    public void init() {
        globalExceptionHandler.addHandlerService(this);
    }

    @Override
    public String serverName() {
        return String.format("[%s]", appName);
    }

    @Override
    public void handle(Throwable e, ErrorLevel level) {
        if (!filter(e)) {
            return;
        }
        switch (level) {
            case INFO:
                log.info(serverName(), throwableToString(e));
                break;
            case WARN:
                log.warn(serverName(), throwableToString(e));
                break;
            case ERROR:
                log.error(serverName(), throwableToString(e));
            case NONE:
                break;
            default:
        }
    }

    @Override
    public boolean filter(Throwable e) {
        return true;
    }
}
