package com.lyloou.component.mqkafka.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.listener.BatchLoggingErrorHandler;
import org.springframework.stereotype.Component;

/**
 * kafka异常统一处理
 *
 * @author lilou
 * @since 2021/8/11
 */
@Slf4j
@Component
@Primary
public class DefaultKafkaExceptionHandler extends BatchLoggingErrorHandler {
    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data) {
        super.handle(thrownException, data);
        log.error("handler error here");
    }

}
