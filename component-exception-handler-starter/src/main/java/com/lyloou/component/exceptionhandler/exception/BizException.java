package com.lyloou.component.exceptionhandler.exception;


import com.lyloou.component.dto.codemessage.CodeMessage;

public class BizException extends CommonException {
    public BizException() {
    }

    public BizException(CodeMessage codeMessage) {
        super(codeMessage.code(), codeMessage.message());
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
