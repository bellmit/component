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

    static String briefErrorMsg(String code, String msg, Throwable e) {
        StackTraceElement se = e.getStackTrace()[0];
        StringBuilder sb = new StringBuilder();
        sb.append("error code:").append(code).append("\n")
                .append(",error msg:").append(msg).append("\n")
                .append(",error name:").append(e.getClass().getName()).append("\n")
                .append(",error happened to class:").append(se.getClassName()).append("\n")
                .append(",error happened to method:").append(se.getMethodName()).append("\n")
                .append(",error happened to line:").append(se.getLineNumber()).append("\n")
        ;
        return sb.toString();
    }

}
