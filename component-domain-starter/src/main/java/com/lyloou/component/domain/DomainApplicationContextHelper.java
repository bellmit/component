package com.lyloou.component.domain;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContextHelper
 */
@Component
public class DomainApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DomainApplicationContextHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> targetClz) {
        T beanInstance = null;
        //优先按type查
        try {
            beanInstance = applicationContext.getBean(targetClz);
        } catch (Exception e) {
            // ignore
        }
        //按name查
        if (beanInstance == null) {
            String simpleName = targetClz.getSimpleName();
            //首字母小写
            simpleName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            //noinspection unchecked
            beanInstance = (T) applicationContext.getBean(simpleName);
        }

        //noinspection ConstantConditions
        if (beanInstance == null) {
            throw new RuntimeException("Component " + targetClz + " can not be found in Spring Container");
        }
        return beanInstance;
    }

    public static Object getBean(String clazz) {
        return DomainApplicationContextHelper.applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return DomainApplicationContextHelper.applicationContext.getBean(name, requiredType);
    }

    @SuppressWarnings("AlibabaVarargsParameter")
    public static <T> T getBean(Class<T> requiredType, Object... params) {
        return DomainApplicationContextHelper.applicationContext.getBean(requiredType, params);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
