package com.lyloou.component.mqrocketmq.support;

import java.lang.annotation.*;

/**
 * @author: ma wei long
 * @date: 2020年7月28日 下午8:56:51
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConsumeVerified {
}