package com.lyloou.component.exceptionhandler.exception;


import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;

public class BizException extends CommonException {

    public BizException() {
        super();
        this.code = CommonCodeMessage.BIZ_ERROR.code();
        this.message = CommonCodeMessage.BIZ_ERROR.message();
    }

    public BizException(CodeMessage codeMessage) {
        super(codeMessage.code(), codeMessage.message());
        this.code = codeMessage.code();
        this.message = codeMessage.message();
    }

    public BizException(String message) {
        super(message);
        this.code = CommonCodeMessage.BIZ_ERROR.code();
        this.message = message;
    }

    public BizException(String code, String message) {
        super(code, message);
        this.code = code;
        this.message = message;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonCodeMessage.BIZ_ERROR.code();
        this.message = message;
    }

    public BizException(String code, String message, Throwable cause) {
        super(code, message, cause);
        this.code = code;
        this.message = message;
    }

}
