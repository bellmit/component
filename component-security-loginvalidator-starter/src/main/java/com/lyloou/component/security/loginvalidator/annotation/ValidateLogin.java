package com.lyloou.component.security.loginvalidator.annotation;

import java.lang.annotation.*;

/**
 * 用来标记需要登录的注解
 *
 * @author lilou
 * @since 2021/7/19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ValidateLogin {

}
