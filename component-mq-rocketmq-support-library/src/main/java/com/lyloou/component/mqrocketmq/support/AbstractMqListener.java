package com.lyloou.component.mqrocketmq.support;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * @author: ma wei long
 * @date: 2020年6月27日 下午2:37:35
 */
@Slf4j
public abstract class AbstractMqListener<T> implements RocketMQListener<MqDTOWrapper<T>> {

    /**
     * 消息执行
     *
     * @param data 消息体
     */
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    @Override
    abstract public void onMessage(MqDTOWrapper<T> data);


    /**
     * 恢复逻辑
     */
    @Recover
    public void recover(Exception ex, Object arg0) {
        //TODO ma wei long 后续可以考虑持久化&报警
        log.error("AbstractMqListener - recover - arg0:{} ex", JSON.toJSONString(arg0), ex);
    }
}
