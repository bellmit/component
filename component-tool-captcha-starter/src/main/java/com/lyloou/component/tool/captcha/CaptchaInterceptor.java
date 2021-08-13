package com.lyloou.component.tool.captcha;

import com.lyloou.component.exceptionhandler.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lilou
 */
@Slf4j
@Component
public class CaptchaInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    CaptchaService captchaService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (ifNeedCheck(handler)) {
            AssertUtil.isTrue(validateCode(new ServletWebRequest(request)), "验证码无效或已过期");
        }
        return true;
    }


    private boolean validateCode(ServletWebRequest servletWebRequest) {
        String captchaKey = servletWebRequest.getParameter("captchaKey");
        String captchaCode = servletWebRequest.getParameter("captchaCode");
        AssertUtil.notEmptyParam(captchaKey, "缺少参数：captchaKey");
        AssertUtil.notEmptyParam(captchaCode, "缺少参数：captchaCode");

        return captchaService.verifyCaptcha(captchaKey, captchaCode);
    }

    private boolean ifNeedCheck(Object handler) {
        // 只对自定义的 controller api统计
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 类上标记了，需要对整个类的方法统计
        if (handlerMethod.getBeanType().getDeclaredAnnotation(CheckCaptcha.class) != null) {
            return true;
        }

        // 方法上标记了需要统计
        if (handlerMethod.getMethodAnnotation(CheckCaptcha.class) != null) {
            return true;
        }
        return false;
    }

}