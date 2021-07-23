package com.lyloou.component.security.signvalidator.annotation;

import java.lang.annotation.*;

/**
 * 用来忽略验证的注解
 *
 * @author lilou
 * @since 2021/7/19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreValidateSign {
}
