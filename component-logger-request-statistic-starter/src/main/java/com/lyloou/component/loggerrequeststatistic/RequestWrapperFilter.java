package com.lyloou.component.loggerrequeststatistic;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 通过 过滤器来
 *
 * @author lilou
 */
public class RequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        }
        if (requestWrapper == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }
}