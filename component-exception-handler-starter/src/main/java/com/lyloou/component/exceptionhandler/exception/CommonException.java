package com.lyloou.component.exceptionhandler.exception;

public class CommonException extends RuntimeException {

    private String code;

    public CommonException() {
        super();
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
