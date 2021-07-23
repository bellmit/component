package com.lyloou.component.security.signvalidator.validator;

import cn.hutool.crypto.digest.DigestUtil;
import com.lyloou.component.security.signvalidator.constant.SignConstant;
import com.lyloou.component.security.signvalidator.exception.SignAssert;
import com.lyloou.component.security.signvalidator.filter.RequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/7/19
 */
public interface SignValidator {

    /**
     * 是否启用签名
     *
     * @param validatorName 验证器名称
     * @return 结果
     */
    boolean isEnabled(String validatorName);

    default Map<String, String> buildParameterMap(String validatorName, HttpServletRequest request, HandlerMethod handlerMethod) {
        Map<String, String> signParamMap = new HashMap<>(10);
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            signParamMap.put(key, request.getParameter(key));
        }

        // 如果有json请求体，拼接为body参数
        boolean hasBody = Arrays.stream(handlerMethod.getMethodParameters())
                .anyMatch(p -> p.hasParameterAnnotation(RequestBody.class));
        if (hasBody) {
            // 注意：需要封装请求体，因为spring中的请求流不可多次使用
            final RequestWrapper requestServlet = new RequestWrapper(request);
            final String bodyString = requestServlet.getBody();
            if (bodyString != null) {
                signParamMap.put(SignConstant.PARAM_BODY, bodyString);
            }
        }
        return signParamMap;
    }

    /**
     * 验证参数的有效性
     *
     * @param validatorName 验证器名称
     * @param signParamMap  请求参数
     * @return 结果
     */
    default boolean checkParameter(String validatorName, Map<String, String> signParamMap) {
        SignAssert.notNull(signParamMap.get(SignConstant.PARAM_CLIENT_ID), "缺少签名的参数 clientId");
        SignAssert.notNull(signParamMap.get(SignConstant.PARAM_TIMESTAMP), "缺少签名的参数 timestamp");
        SignAssert.notNull(signParamMap.get(SignConstant.PARAM_NONCE), "缺少签名的参数 nonce");
        SignAssert.notNull(signParamMap.get(SignConstant.PARAM_SIGN), "缺少签名的参数 sign");
        return true;
    }

    /**
     * 验证时间戳
     *
     * @param validatorName 验证器名称
     * @param timestamp     时间戳
     * @return 结果
     */
    boolean checkTimestamp(String validatorName, Long timestamp);

    /**
     * 验证客户端id，可以根据这个来判断调用端是否合规
     *
     * @param validatorName 验证器名称
     * @param clientId      客户端ID
     * @return 结果
     */
    boolean checkClientId(String validatorName, String clientId);

    /**
     * 获取要用来签名的参数
     *
     * @param signParameterMap 请求参数
     * @return 用&连接的参数
     */
    default String getPreSign(Map<String, String> signParameterMap) {
        return signParameterMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .filter(e -> !StringUtils.isEmpty(e.getValue()))
                .filter(e -> !Objects.equals(e.getKey(), SignConstant.PARAM_SIGN))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    /**
     * 根据参数和密钥生成签名
     *
     * @param signParameterMap 请求参数
     * @param clientSecret     签名
     * @return
     */
    default String buildSign(Map<String, String> signParameterMap, String clientSecret) {
        String preSignAndSecret = getPreSign(signParameterMap) + clientSecret;
        return DigestUtil.md5Hex(preSignAndSecret);
    }

    /**
     * 验证签名
     *
     * @param validatorName    验证器名称
     * @param signParameterMap 请求参数
     * @return 结果
     */
    boolean checkSign(String validatorName, Map<String, String> signParameterMap);

    /**
     * 验证是否可重复
     *
     * @param validatorName    验证器名称
     * @param signParameterMap 请求参数
     * @return
     */
    boolean checkRepeatable(String validatorName, Map<String, String> signParameterMap);

}
