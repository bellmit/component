package com.lyloou.component.exceptionhandler.exception;


import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;

public class ParamException extends CommonException {
    public ParamException() {
        this.code = CommonCodeMessage.ILLEGAL_PARAM.code();
        this.message = CommonCodeMessage.ILLEGAL_PARAM.message();
    }

    public ParamException(CodeMessage codeMessage) {
        super(codeMessage.code(), codeMessage.message());
        this.code = codeMessage.code();
        this.message = codeMessage.message();
    }

    public ParamException(String message) {
        super(message);
        this.code = CommonCodeMessage.ILLEGAL_PARAM.code();
        this.message = message;
    }

    public ParamException(String code, String message) {
        super(code, message);
        this.code = code;
        this.message = message;
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonCodeMessage.ILLEGAL_PARAM.code();
        this.message = message;
    }

    public ParamException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
