package com.lyloou.component.mqrocketmq.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author lilou
 * @since 2021/8/14
 */
@Service
public class PayOrderService {

    @Autowired
    private RocketMqService rocketMqService;

    public void pay() {

        rocketMqService.sendDelayed(Topic.CANCEL_PAY_ORDER,
                new MqDTOWrapper<>(new CancelPayOrderDTO(12L), UUID.randomUUID().toString()),
                MQConstant.DelayLevel.level_2);
        // for (int i = 0; i < 3; i++) {
        // }
    }
}
