package com.lyloou.component.exceptionhandler.service;


import com.lyloou.component.exceptionhandler.model.ErrorLevel;
import com.lyloou.component.exceptionhandler.util.ExceptionUtil;

/**
 * <p>异常处理，可以自己实现此类来进行异常处理</p>
 *
 * @author lyloou
 * @since 2020/12/22
 */
public interface ExceptionHandlerService {
    void init();

    String serverName();

    void handle(Throwable e, ErrorLevel level);

    boolean filter(Throwable e);

    default String throwableToString(Throwable e) {
        return ExceptionUtil.throwableToString(e);
    }

}
