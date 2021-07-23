package com.lyloou.component.security.signvalidator.annotation;

import com.lyloou.component.security.signvalidator.constant.SignConstant;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 用来标记需要验证的注解
 *
 * @author lilou
 * @since 2021/7/19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateSign {
    /**
     * 签名按组配置，默认组为 default
     *
     * @return 验证器名称，默认{@link SignConstant#DEFAULT_VALIDATOR_NAME}
     */
    @AliasFor("value")
    String validatorName() default SignConstant.DEFAULT_VALIDATOR_NAME;

    /**
     * 签名按组配置，默认组为 default
     *
     * @return 验证器名称，默认{@link SignConstant#DEFAULT_VALIDATOR_NAME}
     */
    @AliasFor("validatorName")
    String value() default SignConstant.DEFAULT_VALIDATOR_NAME;
}
