package com.lyloou.component.tool.captcha;

import java.lang.annotation.*;

/**
 * @author lilou
 * @since 2021/7/13
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckCaptcha {
}
