package com.lyloou.tool.captcha;


import com.lyloou.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 处理登录
 */
@RestController
@RequestMapping("captcha")
public class TestCaptchaController {

    @Autowired
    CaptchaService captchaService;

    // 测试异常的情况
    @CheckCaptcha
    @GetMapping(value = "/hello")
    public SingleResponse<String> hello() {
        int i = 1 / 0;
        return SingleResponse.buildSuccess();
    }

    // 测试不需要验证码的情况
    @GetMapping(value = "/ping")
    public SingleResponse<String> ping() {
        return SingleResponse.buildSuccess("pong");
    }

    // 测试需要验证码的情况
    @CheckCaptcha
    @GetMapping(value = "/login")
    public SingleResponse<LoginResponse> login(LoginQuery query) {
        LoginResponse response = new LoginResponse();
        response.setResult(true);
        return SingleResponse.buildSuccess(response);
    }

}
