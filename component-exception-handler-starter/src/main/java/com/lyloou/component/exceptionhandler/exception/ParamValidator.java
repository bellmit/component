package com.lyloou.component.exceptionhandler.exception;

import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author lilou
 * @since 2021/7/16
 */
public class ParamValidator extends Assert {

    public static void isTrue(boolean expression, CodeMessage codeMessage, String message) {
        if (!expression) {
            throw new ParamException(codeMessage.code(), message);
        }
    }

    public static void isTrue(boolean expression, CodeMessage codeMessage) {
        if (!expression) {
            throw new ParamException(codeMessage);
        }
    }

    public static void isTrue(boolean expression, String message) {
        isTrue(expression, CommonCodeMessage.ILLEGAL_PARAM, message);
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void notNull(Object object, CodeMessage codeMessage, String message) {
        if (object == null) {
            throw new ParamException(codeMessage.code(), message);
        }
    }

    public static void notNull(Object object, CodeMessage codeMessage) {
        if (object == null) {
            throw new ParamException(codeMessage);
        }
    }

    public static void notNull(Object object, String message) {
        notNull(object, CommonCodeMessage.ILLEGAL_PARAM, message);
    }

    public static void notNull(Object object) {
        notNull(object, CommonCodeMessage.ILLEGAL_PARAM,
                "[Assertion failed] - the argument " + object + " must not be null");
    }

    public static void notEmpty(Collection<?> collection, CodeMessage codeMessage, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ParamException(codeMessage.code(), message);
        }
    }

    public static void notEmpty(Collection<?> collection, CodeMessage codeMessage) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ParamException(codeMessage);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ParamException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: " +
                "it must contain at least 1 element");
    }
}
