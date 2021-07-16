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

    public static String briefErrorMsg(Throwable e) {
        StackTraceElement se = e.getStackTrace()[0];
        return FormatterUtil.format("" +
                        "=> name: {},\n" +
                        "=> class: {},\n" +
                        "=> method: {},\n" +
                        "=> line: {},\n",
                e.getClass().getName(),
                se.getClassName(),
                se.getMethodName(),
                se.getLineNumber()
        );
    }

}
