package com.lyloou.component.exceptionhandler.util;

import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;
import com.lyloou.component.exceptionhandler.exception.AlertException;
import com.lyloou.component.exceptionhandler.exception.CommonException;
import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * Assertion utility class that assists in validating arguments.
 *
 * <p>Useful for identifying programmer errors early and clearly at runtime.
 *
 * <p>For example, if the contract of a public method states it does not
 * allow {@code null} arguments, {@code Assert} can be used to validate that
 * contract.
 * <p>
 * For example:
 *
 * <pre class="code">
 * Assert.notNull(clazz, "The class must not be null");
 * Assert.isTrue(i > 0, "The value must be greater than zero");</pre>
 * <p>
 * This class is empowered by  {@link org.springframework.util.Assert}
 *
 * @author Frank Zhang
 * @author lilou
 * @date 2019-01-13 11:49 AM
 * @date 2021-05-24 11:49 AM
 */
public abstract class AssertUtil {

    /**
     * Assert a boolean expression, throwing {@code CommonException}
     * <p>
     * for example
     *
     * <pre class="code">Assert.isTrue(i != 0, codeMessage.B_ORDER_illegalNumber,
     * "The order number can not be zero");</pre>
     *
     * @param expression  a boolean expression
     * @param codeMessage 错误码
     * @param message     the exception message to use if the assertion fails
     * @throws CommonException if expression is {@code false}
     */
    public static void isTrue(boolean expression, CodeMessage codeMessage, String message) {
        if (!expression) {
            throw new AlertException(codeMessage.code(), message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        isTrue(expression, CommonCodeMessage.ILLEGAL_DATA, message);
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isFalse(boolean expression, CodeMessage codeMessage, String message) {
        if (expression) {
            throw new AlertException(codeMessage.code(), message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        isFalse(expression, CommonCodeMessage.ILLEGAL_DATA, message);
    }

    public static void isFalse(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be false");
    }

    public static void notEmpty(@Nullable String value, String code, String msg) {
        if (value == null || value.isEmpty()) {
            throw new AlertException(code, msg);
        }
    }

    public static void notEmpty(@Nullable String value, CodeMessage codeMessage) {
        if (value == null || value.isEmpty()) {
            throw new AlertException(codeMessage.code(), codeMessage.message());
        }
    }


    public static void notBlank(@Nullable String value, String code, String msg) {
        if (isBlank(value)) {
            throw new AlertException(code, msg);
        }
    }

    public static void notBlank(@Nullable String value, CodeMessage codeMessage) {
        if (isBlank(value)) {
            throw new AlertException(codeMessage.code(), codeMessage.message());
        }
    }

    public static void notNull(@Nullable Object object, CodeMessage codeMessage, String message) {
        if (object == null) {
            throw new AlertException(codeMessage.code(), message);
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        notNull(object, CommonCodeMessage.ILLEGAL_DATA, message);
    }

    public static void notNull(@Nullable Object object) {
        notNull(object, CommonCodeMessage.ILLEGAL_DATA,
                "[Assertion failed] - the argument " + object + " must not be null");
    }

    public static void notEmpty(@Nullable Collection<?> collection) {
        notEmpty(collection, "列表不能为空");
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new AlertException(message);
        }
    }

    public static void notEmptyParam(@Nullable String value) {
        notEmptyParam(value, "参数错误，不能为空");
    }

    public static void notEmptyParam(@Nullable String value, String message) {
        if (value == null || value.length() == 0) {
            throw new AlertException(CommonCodeMessage.ILLEGAL_PARAM.code(), message);
        }
    }

    public static void notNullParam(@Nullable Object value) {
        if (value == null) {
            throw new AlertException(CommonCodeMessage.ILLEGAL_PARAM.code(), "参数错误，不能为空");
        }
    }

    public static void notNullParam(@Nullable Object value, String message) {
        if (value == null) {
            throw new AlertException(CommonCodeMessage.ILLEGAL_PARAM.code(), message);
        }
    }

    public static void notEmptyParam(@Nullable Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new AlertException(CommonCodeMessage.ILLEGAL_PARAM.code(), message);
        }
    }

    private static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
