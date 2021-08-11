package com.lyloou.component.mqkafka.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author lilou
 * @since 2021/8/11
 */
@SpringBootApplication
@EnableDefaultKafkaClient
public class DemoApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        final DemoProducer producer = context.getBean(DemoProducer.class);
        for (int i = 0; i < 10; i++) {
            final DemoDTO demoDTO = new DemoDTO();
            demoDTO.setName("name-" + i);
            demoDTO.setEmail("example" + i + "@email.com");
            // producer.send("topic-demo", "123", JSONUtil.toJsonStr(demoDTO));
        }
    }
}
