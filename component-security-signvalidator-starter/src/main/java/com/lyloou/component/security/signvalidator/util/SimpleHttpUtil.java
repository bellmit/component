package com.lyloou.component.security.signvalidator.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;

import java.util.HashMap;

/**
 * @author lilou
 * @since 2021/7/21
 */
public class SimpleHttpUtil extends CommonHttpUtil {

    private static String clientId = "clientId-1";
    private static String clientSecret = "clientSecret-1";

    public static void initSecret(String clientId, String clientSecret) {
        SimpleHttpUtil.clientId = clientId;
        SimpleHttpUtil.clientSecret = clientSecret;
    }

    public static String post(String url, String bodyString) {
        return HttpUtil.post(url, bodyString);
    }

    public static String postWithSign(String url, String bodyString) {
        return rawWithSign(url, bodyString, Method.POST, clientId, clientSecret);
    }


    public static String get(String url, HashMap<String, Object> requestParam) {
        return HttpUtil.get(url, requestParam);
    }

    public static String getWithSign(String url, HashMap<String, Object> requestParam) {
        return formWithSign(url, requestParam, Method.GET, clientId, clientSecret);
    }

}
