package com.lyloou.component.dbmysql.support.annotation;

import com.lyloou.component.dbmysql.support.service.MybatisStatisticsLogRecordServiceRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lilou
 * @since 2021/8/10
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MybatisStatisticsLogRecordServiceRegistrar.class})
public @interface EnableMybatisStatisticLogRecord {
    String[] excludedPrefixes() default "";
}
