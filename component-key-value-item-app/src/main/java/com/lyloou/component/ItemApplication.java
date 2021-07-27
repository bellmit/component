package com.lyloou.component;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author lilou
 * @since 2021/4/23
 */
@SpringBootApplication
@EnableSwagger2WebMvc
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }
}
