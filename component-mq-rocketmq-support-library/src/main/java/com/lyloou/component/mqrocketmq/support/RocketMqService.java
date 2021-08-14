package com.lyloou.component.mqrocketmq.support;

/**
 * @author: ma wei long
 * @date: 2020年6月27日 下午12:02:28
 */
public interface RocketMqService {

    /**
     * 转换并发送
     *
     * @param topic 话题，可以参考MQConstant
     * @param data  数据
     */
    void convertAndSend(String topic, MqDTOWrapper<?> data);

    /**
     * 延迟发送
     *
     * @param topic      话题，可以参考MQConstant
     * @param delayLevel 延迟级别，0 不延时   可以参考 MQConstant.DelayLevel的值
     * @param data       数据
     */
    void sendDelayed(String topic, MqDTOWrapper<?> data, int delayLevel);
}
