package com.lyloou.component.loggerrequeststatistic;

import java.lang.annotation.*;

/**
 * @author lilou
 * @since 2021/3/26
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestStatistic {
}
