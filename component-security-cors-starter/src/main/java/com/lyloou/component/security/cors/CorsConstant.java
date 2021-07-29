package com.lyloou.component.security.cors;

import java.util.Collections;
import java.util.List;

/**
 * @author lilou
 * @since 2021/7/29
 */
public class CorsConstant {

    /**
     * Wildcard representing <em>all</em> origins, methods, or headers.
     */
    public static final String ALL = "*";
    public static final String DEFAULT_PATH = "/**";
    public static final List<String> DEFAULT_PERMIT_ALL = Collections.singletonList(ALL);

}
