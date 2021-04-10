package com.lyloou.component.redismanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author lilou
 * @since 2021/4/6
 */
@SpringBootApplication
@EnableCaching
public class RedisManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisManagerApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
