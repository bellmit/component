package com.lyloou.component.mqrocketmq.support;

import com.alibaba.fastjson.JSON;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author: ma wei long
 * @date: 2020年6月27日 下午12:02:58
 */
@Slf4j
@Service
public class RocketMqServiceImpl extends AbstractMqService {

    @Lazy
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void convertAndSend(String topic, MqDTOWrapper<?> data) {
        AssertUtil.notNullParam(topic);
        AssertUtil.notNullParam(data);
        AssertUtil.notNullParam(data.getData());
        rocketMQTemplate.asyncSend(topic, data, new SendCallback() {
            @Override
            public void onSuccess(SendResult res) {
                log.info("convertAndSend - onSuccess - topic：{} data:{} sendResult:{}", topic, JSON.toJSONString(data), JSON.toJSONString(res));
            }

            @Override
            public void onException(Throwable e) {
                log.error("convertAndSend - onException - topic：{} data:{} e:{}", topic, JSON.toJSONString(data), e);
            }
        });
    }

    @Override
    public void sendDelayed(String topic, MqDTOWrapper<?> data, int delayLevel) {
        AssertUtil.notNullParam(topic);
        AssertUtil.notNullParam(data);
        AssertUtil.notNullParam(data.getData());
        AssertUtil.isTrue(delayLevel >= 0, "延迟级别参数必须大于等于0");
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(data).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult res) {
                log.info("sendDelayed - onSuccess - topic：{} data:{} sendResult:{}", topic, JSON.toJSONString(data), JSON.toJSONString(res));
            }

            @Override
            public void onException(Throwable e) {
                log.error("sendDelayed - onException - topic：{} data:{} e:", topic, JSON.toJSONString(data), e);
            }
        }, 5000, delayLevel);
    }
}
