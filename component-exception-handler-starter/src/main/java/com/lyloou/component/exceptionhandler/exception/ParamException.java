package com.lyloou.component.exceptionhandler.exception;


import com.lyloou.component.dto.codemessage.CodeMessage;

public class ParamException extends CommonException {
    public ParamException() {
    }

    public ParamException(CodeMessage codeMessage) {
        super(codeMessage.code(), codeMessage.message());
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String code, String message) {
        super(code, message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
