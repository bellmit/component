package com.lyloou.component.schedulemonitor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 切面
 *
 * @author lilou
 * @since 2021/3/7
 */
@Aspect
@Component
@Slf4j
public class ScheduleMonitorAspect {

    @Autowired
    ScheduleMonitorHandler scheduleMonitorHandler;

    /**
     * The syntax of pointcut : https://blog.csdn.net/zhengchao1991/article/details/53391244
     */
    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {

        final String key = getKey(joinPoint);
        if (scheduleMonitorHandler.isKeyExisted(key)) {
            final Boolean status = scheduleMonitorHandler.getStatus(key);
            if (status != null && !status) {
                log.debug("定时器已经临时停止，key:{}", key);
                return null;
            }
        }

        Object response = null;
        //noinspection
        try {
            response = joinPoint.proceed();
        } catch (Throwable ignored) {
        }

        return response;
    }

    private String getKey(ProceedingJoinPoint pjp) {
        try {
            // 获取类上的名称
            Class<?> classTarget = pjp.getTarget().getClass();
            Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();

            Method method = classTarget.getMethod(pjp.getSignature().getName(), par);
            return getKey(classTarget, method);
        } catch (Exception e) {
            return null;
        }
    }

    private String getKey(Class<?> clazz, Method method) {
        return clazz.getName() + "." + method.getName();
    }

}
