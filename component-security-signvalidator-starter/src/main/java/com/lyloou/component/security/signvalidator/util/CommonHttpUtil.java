package com.lyloou.component.security.signvalidator.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.lyloou.component.security.signvalidator.constant.SignConstant;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/7/21
 */
public class CommonHttpUtil {


    public static String formWithSign(String url, HashMap<String, Object> requestParam, Method method, String clientId, String clientSecret) {
        final HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put(SignConstant.PARAM_CLIENT_ID, clientId);
        paramMap.put(SignConstant.PARAM_TIMESTAMP, System.currentTimeMillis());
        paramMap.put(SignConstant.PARAM_NONCE, IdUtil.fastSimpleUUID());
        requestParam.forEach(paramMap::put);
        String sign = buildSignWithObjectMap(paramMap, clientSecret);
        paramMap.put(SignConstant.PARAM_SIGN, sign);

        final String urlWithForm = HttpUtil.urlWithForm(url, paramMap, Charset.defaultCharset(), false);
        return HttpUtil.createRequest(method, urlWithForm).execute().body();
    }

    public static String rawWithSign(String url, String bodyString, Method method, String clientId, String clientSecret) {
        final HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put(SignConstant.PARAM_CLIENT_ID, clientId);
        paramMap.put(SignConstant.PARAM_TIMESTAMP, System.currentTimeMillis());
        paramMap.put(SignConstant.PARAM_NONCE, IdUtil.fastSimpleUUID());
        paramMap.put(SignConstant.PARAM_BODY, bodyString);
        String sign = buildSignWithObjectMap(paramMap, clientSecret);
        paramMap.put(SignConstant.PARAM_SIGN, sign);
        paramMap.remove(SignConstant.PARAM_BODY);

        final String urlWithForm = HttpUtil.urlWithForm(url, paramMap, Charset.defaultCharset(), false);
        return HttpUtil.createRequest(method, urlWithForm).body(bodyString).execute().body();
    }

    public static String getPreSign(Map<String, String> signParameterMap) {
        return signParameterMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .filter(e -> !StrUtil.isEmpty(e.getValue()))
                .filter(e -> !Objects.equals(e.getKey(), SignConstant.PARAM_SIGN))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    public static Map<String, String> convertToStringMap(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>();
        map.forEach((k, v) -> result.put(k, Objects.isNull(v) ? null : String.valueOf(v)));
        return result;
    }

    public static String buildSignWithStringMap(Map<String, String> signParameterMap, String clientSecret) {
        String preSignAndSecret = getPreSign(signParameterMap) + clientSecret;
        return DigestUtil.md5Hex(preSignAndSecret);
    }

    public static String buildSignWithObjectMap(Map<String, Object> signParameterMap, String clientSecret) {
        return buildSignWithStringMap(convertToStringMap(signParameterMap), clientSecret);
    }
}
