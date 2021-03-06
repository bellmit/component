package com.lyloou.component.security.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static com.lyloou.component.security.cors.CorsConstant.DEFAULT_PATH;
import static com.lyloou.component.security.cors.CorsConstant.DEFAULT_PERMIT_ALL;


/**
 * ref: {@link org.springframework.web.cors.CorsConfiguration}
 *
 * @author lilou
 */
@Data
@ConfigurationProperties(prefix = "component.cors")
public class CorsItemProperties {

    /**
     * Register a {@link org.springframework.web.cors.CorsConfiguration} for the specified path pattern.
     */
    private String path = DEFAULT_PATH;

    /**
     * Whether user credentials are supported.
     */
    private boolean allowCredentials = true;
    /**
     * Set the origins to allow, e.g. {@code "https://domain1.com"}.
     * <p>The special value {@code "*"} allows all domains.
     * <p>By default this is {@code {"*"}}.
     */
    private List<String> allowedOrigins = DEFAULT_PERMIT_ALL;

    /**
     * Set the HTTP methods to allow, e.g. {@code "GET"}, {@code "POST"},
     * {@code "PUT"}, etc.
     * <p>The special value {@code "*"} allows all methods.
     * <p>If not set, {@code "*"} are allowed.
     */
    private List<String> allowedMethods = DEFAULT_PERMIT_ALL;

    /**
     * Set the list of headers that a pre-flight request can list as allowed
     * for use during an actual request.
     * <p>The special value {@code "*"} allows actual requests to send any
     * header.
     * <p>A header name is not required to be listed if it is one of:
     * {@code Cache-Control}, {@code Content-Language}, {@code Expires},
     * {@code Last-Modified}, or {@code Pragma}.
     * <p>By default this is {@code {"*"}}.
     */
    private List<String> allowedHeaders = DEFAULT_PERMIT_ALL;

    /**
     * Configure how long, as a duration, the response from a pre-flight request
     * can be cached by clients.
     * <p>By default this is {@code 1800}.
     */
    private Long maxAge = 1800L;
}