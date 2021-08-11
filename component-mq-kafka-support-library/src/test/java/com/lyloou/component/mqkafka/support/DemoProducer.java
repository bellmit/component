package com.lyloou.component.mqkafka.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lilou
 * @since 2021/8/11
 */
@Component
@Slf4j
public class DemoProducer {
    @Autowired
    private KafkaTemplate<String, String> template;

    public void send(String topic, String key, String data) {
        template.send(topic, key, data);
        log.info("==> sendMessage, topic:{},key{},data,{}", topic, key, data);
    }
}
