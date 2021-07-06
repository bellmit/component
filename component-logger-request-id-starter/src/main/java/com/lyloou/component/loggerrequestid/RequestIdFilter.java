package com.lyloou.component.loggerrequestid;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * [SLF4J Tool: A servlet filter that adds a generated unique request id to the logging context (MDC)](https://gist.github.com/pismy/509289dc5822df8bf272)
 * <p>
 * A {@link Filter servlet filter} that adds a generated unique request id to the logging context ({@link MDC})
 * <p>
 * Requires SLF4J as the logging facade API.
 * <p>
 * With a log management system such as ELK, this will help you track a complete callflow, filtering logs from a unique request. Quite valuable when
 * your system processes thousands of requests per second and produces terabytes of logs each day...
 * <p>
 * Note that in a micro-services architecture, upon calling other services you can transfer this generated {@code requestId} in a request header
 * {@code X-Track-RequestId}, thus implementing an end-to-end callflow tracking.
 * <p>
 * The request attribute, MDC attribute and request header can be overridden with Java properties:
 *
 * <table border=1>
 * <tr>
 * <th>attribute</th>
 * <th>default value</th>
 * <th>Filter config</th>
 * <th>Java property</th>
 * </tr>
 * <tr>
 * <td>request header</td>
 * <td>{@code X-Track-RequestId}</td>
 * <td>{@code header.requestId}</td>
 * <td>{@code slf4j.tools.header.requestId}</td>
 * </tr>
 * <tr>
 * <td>request attribute</td>
 * <td>{@code track.requestId}</td>
 * <td>{@code attribute.requestId}</td>
 * <td>{@code slf4j.tools.attribute.requestId}</td>
 * </tr>
 * <tr>
 * <td>MDC attribute</td>
 * <td>{@code requestId}</td>
 * <td>{@code mdc.requestId}</td>
 * <td>{@code slf4j.tools.mdc.requestId}</td>
 * </tr>
 * </table>
 * <p>
 * y {@code slf4j.tools.mdc.sessionId}
 *
 * @author pismy
 */
@Slf4j
public class RequestIdFilter implements Filter {

    private String headerName;
    private String attributeName;
    private String mdcName;
    private final Snowflake snowflake = IdUtil.getSnowflake(0, 0);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        headerName = getConfig("header.requestId", "X-Track-RequestId", filterConfig);
        attributeName = getConfig("attribute.requestId", "track.requestId", filterConfig);
        mdcName = getConfig("mdc.requestId", "requestId", filterConfig);
    }

    private String getConfig(String param, String defaultValue, FilterConfig filterConfig) {
        String valueFromConfig = filterConfig.getInitParameter(param);
        return valueFromConfig != null ? valueFromConfig : System.getProperty("slf4j.tools." + param, defaultValue);
    }

    /**
     * <ul>
     * <li>checks whether the current request has an attached request id,
     * <li>if not, tries to get one from request headers (implements end-to-end callflow traceability),
     * <li>if not, generates one
     * <li>attaches it to the request (as an attribute) and to the {@link MDC} context.
     * </ul>
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestId = getReqId(request);
        request.setAttribute(attributeName, requestId);

        // attach to MDC context
        MDC.put(mdcName, requestId);

        try {
            if (response instanceof HttpServletResponse) {
                doFilterWithRequestId(request, response, chain, requestId);
            } else {
                chain.doFilter(request, response);
            }
        } finally {
            // remove from MDC context
            MDC.remove(mdcName);
        }
    }

    private void doFilterWithRequestId(ServletRequest request, ServletResponse response, FilterChain chain, String reqId) throws IOException, ServletException {
        //转换成代理类
        ResponseWrapper wrapperResponse = new ResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, wrapperResponse);
        //获取返回值
        byte[] content = wrapperResponse.getContent();
        content = addRequestIdToContentIfNeed(reqId, content);

        //把返回值输出到客户端
        ServletOutputStream out = response.getOutputStream();
        out.write(content);
        out.flush();
        out.close();
    }

    private byte[] addRequestIdToContentIfNeed(String requestId, byte[] content) {
        //判断是否有值
        if (content.length == 0) {
            return content;
        }

        try {
            final String strContent = new String(content, StandardCharsets.UTF_8);
            if (maybeJsonString(strContent)) {
                final JSONObject jsonContent = new JSONObject(strContent);
                jsonContent.set("requestId", requestId);
                content = jsonContent.toString().getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            //ignore
        }
        return content;
    }

    private boolean maybeJsonString(String strContent) {
        final boolean isStartOk = strContent.startsWith("[") || strContent.startsWith("{");
        final boolean isEndOk = strContent.endsWith("]") || strContent.endsWith("}");
        return isStartOk && isEndOk;
    }

    private String getReqId(ServletRequest request) {
        // checks whether the current request has an attached request id
        String reqId = (String) request.getAttribute(attributeName);
        if (reqId == null) {
            // retrieve id from request headers
            if (request instanceof HttpServletRequest) {
                reqId = ((HttpServletRequest) request).getHeader(headerName);
            }
            if (reqId == null) {
                // no requestId (either from attributes or headers): generate one
                reqId = snowflake.nextIdStr();
            }
            // attach to request
        }
        return reqId;
    }

    @Override
    public void destroy() {
    }
}