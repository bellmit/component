package com.lyloou.component.tool.captcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author lilou
 * @since 2021/7/13
 */
@SpringBootApplication(scanBasePackages = "com.lyloou.tool.captcha")
public class CaptchaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaptchaApplication.class, args);
    }

    @Bean
    public CaptchaService captchaService() {
        return new CaptchaServiceImpl();
    }
}
