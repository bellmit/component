package com.lyloou.component.mqrocketmq.support;

import com.alibaba.fastjson.JSON;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author: ma wei long
 * @date: 2020年7月28日 下午8:58:00
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ConsumeVerifiedAdvice {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.lyloou.component.mqrocketmq.support.ConsumeVerified)")
    private void consumeVerified() {
    }

    @Around("consumeVerified()")
    public void intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object o = args[0];

        MqDTOWrapper<?> data = (MqDTOWrapper<?>) o;
        String messageId = data.getMessageId();
        AssertUtil.isTrue(null != messageId, "messageId is null");

        if (redisTemplate.opsForHash().hasKey(MQConstant.MQ_CHECK_KEY, messageId)) {
            log.warn("ConsumeVerifiedAdvice - 消息重复消费：data:{}", JSON.toJSONString(data));
            return;
        }

        joinPoint.proceed();

        redisTemplate.opsForHash().put(MQConstant.MQ_CHECK_KEY, messageId, messageId);
        //数据多了可以设置个比较长的有效期  或者持久化到 其他地方 或者定期清理下数据
        redisTemplate.expire(MQConstant.MQ_CHECK_KEY, 30, TimeUnit.DAYS);
    }
}
