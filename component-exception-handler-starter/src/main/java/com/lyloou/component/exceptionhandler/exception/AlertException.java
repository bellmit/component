package com.lyloou.component.exceptionhandler.exception;

import com.lyloou.component.dto.codemessage.CommonCodeMessage;

/**
 * 提示性错误（可以直接返回给前端显示的那种提示）
 */
public class AlertException extends RuntimeException {

    protected String code = CommonCodeMessage.ERROR.code();
    protected String message = CommonCodeMessage.ERROR.message();

    public AlertException() {
        super();
    }

    public AlertException(String message) {
        super(message);
        this.message = message;
    }

    public AlertException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public AlertException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public AlertException(String code, String message, Throwable cause) {
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
