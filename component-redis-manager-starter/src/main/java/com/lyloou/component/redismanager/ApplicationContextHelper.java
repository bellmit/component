package com.lyloou.component.redismanager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * ApplicationContextHelper
 * PriorityOrdered:[问题记录，为什么自定义的注解加在类上，此类在Bean初始化完成之后的postProcessAfterInitialization方法中查不到头上的注解？_wu_xiaozhi_xiao的博客-CSDN博客](https://blog.csdn.net/wu_xiaozhi_xiao/article/details/114394810)
 *
 * @author lyloou
 * @date 2021-03-06 9:45
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware, BeanPostProcessor, PriorityOrdered {
    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> targetClz) {
        return applicationContext.getBean(targetClz);
    }

    public static Object getBean(String claz) {
        return ApplicationContextHelper.applicationContext.getBean(claz);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return ApplicationContextHelper.applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... params) {
        return ApplicationContextHelper.applicationContext.getBean(requiredType, params);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        final Method[] declaredMethods = bean.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            final CacheConfig cacheConfig = declaredMethod.getDeclaringClass().getAnnotation(CacheConfig.class);

            final Caching caching = declaredMethod.getAnnotation(Caching.class);
            if (caching != null) {
                final Cacheable[] cacheableArray = caching.cacheable();
                if (cacheableArray.length > 0) {
                    for (Cacheable cacheable : cacheableArray) {
                        doPutPrefix(cacheConfig, cacheable);
                    }
                }
            }

            final Cacheable cacheable = declaredMethod.getAnnotation(Cacheable.class);
            doPutPrefix(cacheConfig, cacheable);
        }
        return bean;
    }

    private void doPutPrefix(CacheConfig cacheConfig, Cacheable cacheable) {

        if (cacheable == null) {
            return;
        }

        RedisManagerService handler = applicationContext.getBean(RedisManagerService.class);
        String[] cacheNames = getCacheNames(cacheConfig, cacheable);
        for (String cacheName : cacheNames) {
            handler.putPrefix(cacheName, true);
        }
    }

    private String[] getCacheNames(CacheConfig cacheConfig, Cacheable cacheable) {
        return Stream.of(cacheable.cacheNames(), cacheable.value(), cacheConfig.cacheNames())
                .filter(strings -> strings.length > 0)
                .findFirst()
                .orElse(new String[]{});
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
