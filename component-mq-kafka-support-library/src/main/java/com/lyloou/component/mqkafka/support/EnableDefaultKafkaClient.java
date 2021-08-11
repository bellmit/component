package com.lyloou.component.mqkafka.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lilou
 * @since 2021/8/11
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DefaultKafkaConsumer.class, DefaultKafkaProducer.class, DefaultKafkaExceptionHandler.class})
public @interface EnableDefaultKafkaClient {
}
