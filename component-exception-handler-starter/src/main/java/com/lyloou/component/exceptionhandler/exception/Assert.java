package com.lyloou.component.exceptionhandler.exception;

import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;

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
public abstract class Assert {

    /**
     * Assert a boolean expression, throwing {@code BizException}
     * <p>
     * for example
     *
     * <pre class="code">Assert.isTrue(i != 0, codeMessage.B_ORDER_illegalNumber,
     * "The order number can not be zero");</pre>
     *
     * @param expression  a boolean expression
     * @param codeMessage 错误码
     * @param message     the exception message to use if the assertion fails
     * @throws BizException if expression is {@code false}
     */
    public static void isTrue(boolean expression, CodeMessage codeMessage, String message) {
        if (!expression) {
            throw new BizException(codeMessage.code(), codeMessage.appendMessage(message));
        }
    }

    public static void isTrue(boolean expression, String message) {
        isTrue(expression, CommonCodeMessage.ILLEGAL_DATA, message);
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void notNull(Object object, CodeMessage codeMessage, String message) {
        if (object == null) {
            throw new BizException(codeMessage.code(), codeMessage.appendMessage(message));
        }
    }

    public static void notNull(Object object, String message) {
        notNull(object, CommonCodeMessage.ILLEGAL_DATA, message);
    }

    public static void notNull(Object object) {
        notNull(object, CommonCodeMessage.ILLEGAL_DATA,
                "[Assertion failed] - the argument " + object + " must not be null");
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection,
                "[Assertion failed] - this collection must not be empty: " +
                        "it must contain at least 1 element");
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (collection != null && collection.size() > 0) {
            throw new BizException(message);
        }
    }

}
