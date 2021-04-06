package com.lyloou.component.redismanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

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
}
