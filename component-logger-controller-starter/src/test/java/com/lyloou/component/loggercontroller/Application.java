package com.lyloou.component.loggercontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author lilou
 * @since 2021/3/9
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void logIt() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        log.trace("trace");
    }
}
