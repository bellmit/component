package com.lyloou.component.mqkafka.support;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lilou
 * @since 2021/8/11
 */
@Component
@Slf4j
public class DemoConsumer {

    @KafkaListener(topics = {"topic-demo"}, containerFactory = "ackSingleContainerFactory")
    public void listen(List<ConsumerRecord<String, String>> consumerRecords, Acknowledgment ack) {
        if (CollectionUtils.isEmpty(consumerRecords)) {
            return;
        }
        int i = 1 / 0;
        // doData
        doConsumerRecordList(consumerRecords);
        ack.acknowledge();
    }

    private void doConsumerRecordList(List<ConsumerRecord<String, String>> consumerRecords) {
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            final String data = consumerRecord.value();
            final DemoDTO demoDTO = JSON.parseObject(data, DemoDTO.class);
            // do dto
            log.info("get msg==>{}", demoDTO);
        }
    }
}
