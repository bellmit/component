package com.lyloou.component.exceptionhandler.util;

/**
 * <p>异常工具类</p>
 *
 * @author lyloou
 * @since 2020/12/22
 */
public class ExceptionUtil {
    public static String throwableToString(Throwable e) {
        if (e == null) {
            return null;
        }

        StringBuilder trace = new StringBuilder(e.toString());
        trace.append("\n");
        for (StackTraceElement e1 : e.getStackTrace()) {
            trace.append("\t at ").append(e1.toString()).append("\n");
        }
        return trace.toString();
    }
}
