package com.lyloou.component.exceptionhandler.handler;

import com.lyloou.component.exceptionhandler.model.ErrorLevel;
import com.lyloou.component.exceptionhandler.service.ExceptionHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.util.List;

/**
 * <p>处理定时任务出错时的错误捕获</p>
 *
 * @author lyloou
 * @since 2020/12/22
 */
@Component("scheduleErrorHandler")
public class ScheduleTaskErrorHandler implements ErrorHandler {

    @Autowired
    CommonExceptionHandler globalExceptionHandler;


    @Override
    public void handleError(@NonNull Throwable throwable) {
        final List<ExceptionHandlerService> serviceList = globalExceptionHandler.getHandlerServiceList();
        for (ExceptionHandlerService service : serviceList) {
            service.handle(throwable, ErrorLevel.ERROR);
        }
    }
}
