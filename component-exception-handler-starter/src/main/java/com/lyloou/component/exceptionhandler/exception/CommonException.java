package com.lyloou.component.exceptionhandler.exception;

import com.lyloou.component.dto.codemessage.CommonCodeMessage;

public class CommonException extends RuntimeException {

    protected String code = CommonCodeMessage.ERROR.code();
    protected String message = CommonCodeMessage.ERROR.message();

    public CommonException() {
        super();
    }

    public CommonException(String message) {
        super(message);
        this.message = message;
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
