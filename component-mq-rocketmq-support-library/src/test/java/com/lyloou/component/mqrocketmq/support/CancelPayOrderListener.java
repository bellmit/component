package com.lyloou.component.mqrocketmq.support;

import com.alibaba.fastjson.JSON;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author: ma wei long
 * @date: 2020年6月27日 下午2:35:42
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = Topic.CANCEL_PAY_ORDER, consumerGroup = "edu_group" + "_" + Topic.CANCEL_PAY_ORDER)
public class CancelPayOrderListener extends AbstractMqListener<CancelPayOrderDTO> {

    /**
     *
     */
    @Override
    @ConsumeVerified
    public void onMessage(MqDTOWrapper<CancelPayOrderDTO> data) {
        log.info("onMessage - data:{}", JSON.toJSONString(data));
        CancelPayOrderDTO cancelPayOrderDTO = data.getData();
        AssertUtil.notNullParam(cancelPayOrderDTO);

        int i = 1 / 0;
        Long orderId = cancelPayOrderDTO.getOrderId();
        AssertUtil.notNullParam(orderId);

        // do orderId
        log.info("orderId: {}", orderId);
    }

}