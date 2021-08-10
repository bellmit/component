package com.lyloou.component.dbmysql.support.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpServletUtil {
    /**
     * 得到HttpServletRequest对象
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    /**
     * 设置父线程requestAttributes共享 当异步执行的DAO方法需要记录日志时,需要先调用此方法设置
     */
    public static void setParentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            RequestContextHolder.setRequestAttributes(requestAttributes, true);
        }
    }

}
