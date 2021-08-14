package com.lyloou.component.mqrocketmq.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author lilou
 * @since 2021/8/14
 */
@SpringBootApplication
@EnableRetry
public class CancelPayOrderApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(CancelPayOrderApplication.class, args);
        final PayOrderService payOrderService = context.getBean(PayOrderService.class);
        System.out.println("----->");
        payOrderService.pay();
    }
}
