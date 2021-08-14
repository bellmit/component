package com.lyloou.component.mqrocketmq.support;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * 如果需要用的重试和恢复机制，需要开启重试功能
 * <code>@{@link org.springframework.retry.annotation.EnableRetry}</code>
 * <p>
 */
@Slf4j
public abstract class AbstractMqService implements RocketMqService {

    /**
     * 转换并发送
     *
     * @param topic 话题
     * @param data  数据
     */
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    @Override
    abstract public void convertAndSend(String topic, MqDTOWrapper<?> data);

    /**
     * 延迟发送
     *
     * @param topic      话题
     * @param delayLevel 延迟级别
     * @param data       数据
     */
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    @Override
    abstract public void sendDelayed(String topic, MqDTOWrapper<?> data, int delayLevel);

    /**
     * 恢复异常
     *
     * @param ex   异常
     * @param arg0 参数0
     * @param arg1 参数1
     */
    @Recover
    public void recover(Exception ex, Object arg0, Object arg1) {
        //TODO ma wei long 后续可以考虑持久化&报警
        log.error("AbstractMqService - recover - args0:{} arg1:{} ex", JSON.toJSONString(arg0), JSON.toJSONString(arg1), ex);
    }
}
