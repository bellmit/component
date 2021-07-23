package com.lyloou.component.security.signvalidator.exception;

/**
 * @author lilou
 * @since 2021/7/19
 */
public class SignException extends RuntimeException {
    public SignException() {
    }

    public SignException(String message) {
        super(message);
    }

    public SignException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignException(Throwable cause) {
        super(cause);
    }

}