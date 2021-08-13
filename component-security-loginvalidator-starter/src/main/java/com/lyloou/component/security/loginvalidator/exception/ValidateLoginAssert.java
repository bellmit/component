package com.lyloou.component.security.loginvalidator.exception;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author lilou
 * @since 2021/7/16
 */
public class ValidateLoginAssert extends Assert {


    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ValidateLoginException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }


    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ValidateLoginException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - the argument " + object + " must not be null");
    }


    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ValidateLoginException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: " +
                "it must contain at least 1 element");
    }
}
