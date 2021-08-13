package com.lyloou.component.security.loginvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author lilou
 * @since 2021/8/12
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ValidateLoginApplication {
    public static void main(String[] args) {
        SpringApplication.run(ValidateLoginApplication.class, args);
    }
}
