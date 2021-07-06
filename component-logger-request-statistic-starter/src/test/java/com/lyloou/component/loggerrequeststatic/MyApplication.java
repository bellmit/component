package com.lyloou.component.loggerrequeststatic;

import com.lyloou.component.loggerrequeststatistic.RequestStatistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lilou
 * @since 2021/7/6
 */
@SpringBootApplication
@RestController
@Slf4j
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class);
    }

    @RequestStatistic
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
