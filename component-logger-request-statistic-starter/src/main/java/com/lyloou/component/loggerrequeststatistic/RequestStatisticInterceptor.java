package com.lyloou.component.loggerrequeststatistic;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * @author lilou
 */
@Slf4j
public class RequestStatisticInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (ifNeedStatistic(handler)) {
            request.setAttribute(Constant.START_TIME, System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ifNeedStatistic(handler)) {
            long startTime = (Long) request.getAttribute(Constant.START_TIME);
            long endTime = System.currentTimeMillis();
            final long takenTime = endTime - startTime;
            log.info("\n【请求信息统计】\n" +
                            "==> Request URL: {}\n" +
                            "==> Start Time: {}\n" +
                            "==> End Time: {}\n" +
                            "==> Taken Time: {}ms\n" +
                            "==> Replay Curl: {}\n",
                    request.getRequestURL(),
                    new Date(startTime),
                    new Date(endTime),
                    takenTime,
                    replayUrlInfo(request)
            );
        }
    }

    private boolean ifNeedStatistic(Object handler) {
        // 只对自定义的 controller api统计
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 类上标记了需要统计
        if (handlerMethod.getBeanType().getDeclaredAnnotation(RequestStatistic.class) != null) {
            return true;
        }

        // 方法上标记了需要统计
        if (handlerMethod.getMethodAnnotation(RequestStatistic.class) != null) {
            return true;
        }
        return false;
    }


    /**
     * 获取重放信息
     *
     * @param request 请求信息
     * @return 重放 URL 信息，以 curl 的方式返回
     */
    protected String replayUrlInfo(HttpServletRequest request) {
        // 示例
        // curl -X POST "http://localhost:8080/asmodeus/admin/playlist/copy" -H "accept: */*" -H "Authorization: eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MTY3Njg4NjYsInVzZXJJZCI6MSwibG9naW5OYW1lIjoiYWRtaW4ifQ.d1z4KBOd5Z9PuOr6D8fIe-QrOejpHDPFAeAPIWNqbDoLr-N3t3ujsuGwJgSJNHeOmBakfhFyuV5QA69O_M5Z9g" -H "Content-Type: application/json" -d "{ \"playlistId\": 1}"
        // curl -X GET "http://localhost:8080/asmodeus/admin/playlist/list?pageNo=1&pageSize=1&playStatus=1" -H "accept: */*" -H "Authorization: eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MTY3Njg4NjYsInVzZXJJZCI6MSwibG9naW5OYW1lIjoiYWRtaW4ifQ.d1z4KBOd5Z9PuOr6D8fIe-QrOejpHDPFAeAPIWNqbDoLr-N3t3ujsuGwJgSJNHeOmBakfhFyuV5QA69O_M5Z9g"

        List<String> list = new ArrayList<>();
        list.add("CURL");

        list.add("-X");
        list.add(request.getMethod());

        StringBuffer url = request.getRequestURL();
        final String queryString = request.getQueryString();
        if (Strings.isNotEmpty(queryString)) {
            url.append("?").append(queryString);
        }
        list.add("\"" + url + "\"");

        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            // 过滤掉内容长度
            if ("content-length".equalsIgnoreCase(headerName)) {
                continue;
            }
            list.add("-H");
            list.add(String.format("\"%s:%s\"", headerName, request.getHeader(headerName)));
        }

        final RequestWrapper requestServlet = new RequestWrapper(request);
        final String bodyString = requestServlet.getBody();
        if (Strings.isNotEmpty(bodyString)) {
            list.add("-d");
            list.add(String.format("\"%s\"", bodyString.replaceAll("\"", "\\\\\"")));
        }
        return String.join(" ", list);
    }

    public static void main(String[] args) {
        String s = "{" + "\"playlistId\": 1" + "} ";
        System.out.println(s.replaceAll("\"", "\\\\\""));
    }
}