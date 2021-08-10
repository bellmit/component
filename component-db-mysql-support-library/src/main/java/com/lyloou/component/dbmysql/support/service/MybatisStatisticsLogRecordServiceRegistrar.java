package com.lyloou.component.dbmysql.support.service;

import com.lyloou.component.dbmysql.support.annotation.EnableMybatisStatisticLogRecord;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * [Spring-Boot之@Enable*注解的工作原理 - 简书](https://www.jianshu.com/p/3da069bd865c)
 *
 * @author lilou
 * @since 2021/8/10
 */
public class MybatisStatisticsLogRecordServiceRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        final Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableMybatisStatisticLogRecord.class.getName());
        if (CollectionUtils.isEmpty(attributes)) {
            return;
        }

        final Object excludedPrefixesObject = attributes.getOrDefault("excludedPrefixes", new String[0]);
        final List<String> excludedPrefixes = Arrays.asList((String[]) excludedPrefixesObject);
        final BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(MybatisStatisticsLogRecordServiceImpl.class);
        beanDefinition.addPropertyValue("excludedPrefixes", excludedPrefixes);
        registry.registerBeanDefinition(MybatisStatisticsLogRecordServiceImpl.class.getName(), beanDefinition.getBeanDefinition());
    }
}
