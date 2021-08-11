package com.lyloou.component.mqkafka.support;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * http://bcxw.net/article/545.html
 *
 * @author lilou
 */
@EnableKafka
public class DefaultKafkaConsumer {
    @Autowired
    private KafkaProperties kafkaProperties;

    /**
     * Description：获取配置
     * Date：        2017年7月11日
     *
     * @author shaqf
     */
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // 172.20.154.101:9092,172.20.154.103:9092,172.20.154.104:9092
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", kafkaProperties.getBootstrapServers()));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "50");
        return props;
    }

    /**
     * 获取工厂
     */
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    /**
     * 自定义 支持并发消费的消费容器工厂，
     * 可以提供给@KafkaListener来实现多线程消费
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean("ackConcurrencyContainerFactory")
    @Primary
    public ConcurrentKafkaListenerContainerFactory ackConcurrencyContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setBatchListener(true);
        factory.setConcurrency(5);
        factory.setBatchErrorHandler(new DefaultKafkaExceptionHandler());
        factory.getContainerProperties().setPollTimeout(5000);
        return factory;
    }

    /**
     * 自定义单例消费的消费容器工厂，
     * 可以提供给@KafkaListener来实现单线程消费
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean("ackSingleContainerFactory")
    public ConcurrentKafkaListenerContainerFactory ackSingleContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setBatchListener(true);
        factory.setBatchErrorHandler(new DefaultKafkaExceptionHandler());
        factory.getContainerProperties().setPollTimeout(5000);
        return factory;
    }
}