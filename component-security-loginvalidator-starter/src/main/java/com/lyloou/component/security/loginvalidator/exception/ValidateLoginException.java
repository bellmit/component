package com.lyloou.component.security.loginvalidator.exception;

/**
 * @author lilou
 * @since 2021/7/19
 */
public class ValidateLoginException extends RuntimeException {
    public ValidateLoginException() {
    }

    public ValidateLoginException(String message) {
        super(message);
    }

    public ValidateLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateLoginException(Throwable cause) {
        super(cause);
    }

}