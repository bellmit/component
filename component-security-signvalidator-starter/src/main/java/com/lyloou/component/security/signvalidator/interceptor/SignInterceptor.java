package com.lyloou.component.security.signvalidator.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import com.lyloou.component.security.signvalidator.annotation.IgnoreValidateSign;
import com.lyloou.component.security.signvalidator.annotation.ValidateSign;
import com.lyloou.component.security.signvalidator.constant.SignConstant;
import com.lyloou.component.security.signvalidator.exception.SignAssert;
import com.lyloou.component.security.signvalidator.exception.SignException;
import com.lyloou.component.security.signvalidator.properties.SignProperties;
import com.lyloou.component.security.signvalidator.properties.ValidatorConfig;
import com.lyloou.component.security.signvalidator.validator.SignValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

import static com.lyloou.component.security.signvalidator.constant.SignConstant.DEFAULT_SIGN_VALIDATOR;
import static com.lyloou.component.security.signvalidator.constant.SignConstant.DEFAULT_VALIDATOR_NAME;

/**
 * @author lilou
 */
@Slf4j
@Component
public class SignInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 只对自定义的 controller api统计
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 忽略验证的接口
        final IgnoreValidateSign ignoreValidateSign = Optional.ofNullable(
                // 类上标记了，不需要对整个类的方法验证
                handlerMethod.getBeanType().getDeclaredAnnotation(IgnoreValidateSign.class))
                // 方法上标记了不需要验证
                .orElseGet(() -> handlerMethod.getMethodAnnotation(IgnoreValidateSign.class));
        if (ignoreValidateSign != null) {
            return true;
        }

        final SignProperties signProperties = SpringUtil.getBean(SignProperties.class);
        final Boolean ignoreValidate = signProperties.getIgnoreValidate();
        SignAssert.notNull(ignoreValidate, "无效的ignoreValidate配置");

        final ValidateSign validateSign = Optional.ofNullable(
                // 类上标记了，需要对整个类的方法验证
                handlerMethod.getBeanType().getDeclaredAnnotation(ValidateSign.class))
                // 方法上标记了需要验证
                .orElseGet(() -> handlerMethod.getMethodAnnotation(ValidateSign.class));
        if (validateSign == null && ignoreValidate) {
            return true;
        }

        final String validatorName = Optional.ofNullable(validateSign)
                .map(ValidateSign::value)
                .orElse(DEFAULT_VALIDATOR_NAME);

        final String validatorBeanName = Optional.ofNullable(signProperties.getValidators()
                .get(validatorName))
                .map(ValidatorConfig::getBeanName)
                .orElse(DEFAULT_SIGN_VALIDATOR);

        Map<String, SignValidator> signValidatorMap = SpringUtil.getBeansOfType(SignValidator.class);
        final SignValidator validator = signValidatorMap.get(validatorBeanName);
        SignAssert.notNull(validator, "无效的签名验证器：" + validatorBeanName);

        // 1. 是否开启签名验证
        if (!validator.isEnabled(validatorName)) {
            return true;
        }

        // 获取请求参数
        final Map<String, String> parameterMap = validator.buildParameterMap(validatorName, request, handlerMethod);

        // 2. 验证参数的有效性
        if (!validator.checkParameter(validatorName, parameterMap)) {
            throw new SignException("无效的签名参数");
        }

        // 3. 验证客户端有效性
        final String clientId = parameterMap.get(SignConstant.PARAM_CLIENT_ID);
        if (!validator.checkClientId(validatorName, clientId)) {
            throw new SignException("无效的客户端id");
        }

        // 4. 请求的时间是否在限制的时间内（如：1 分钟内，单位毫秒）
        final long timestamp = Long.parseLong(parameterMap.get(SignConstant.PARAM_TIMESTAMP));
        if (!validator.checkTimestamp(validatorName, timestamp)) {
            throw new SignException("无效的时间戳");
        }

        // 5. 验证签名
        if (!validator.checkSign(validatorName, parameterMap)) {
            throw new SignException("签名失败");
        }

        // 6. 验证重复请求
        if (!validator.checkRepeatable(validatorName, parameterMap)) {
            throw new SignException("不可重复请求");
        }

        return true;
    }


}