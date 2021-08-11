package com.lyloou.component.exceptionhandler.exception;

import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;

/**
 * @author lilou
 * @since 2021/8/11
 */
public class MessageQueueException extends CommonException {
    private Object data;

    public MessageQueueException() {
        this.code = CommonCodeMessage.MQ_ERROR.code();
        this.message = CommonCodeMessage.MQ_ERROR.message();
    }

    public MessageQueueException(CodeMessage codeMessage) {
        super(codeMessage.code(), codeMessage.message());
        this.code = codeMessage.code();
        this.message = codeMessage.message();
    }

    public MessageQueueException(String message) {
        super(message);
        this.code = CommonCodeMessage.MQ_ERROR.code();
        this.message = message;
    }

    public MessageQueueException(String code, String message) {
        super(code, message);
        this.code = code;
        this.message = message;
    }

    public MessageQueueException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonCodeMessage.MQ_ERROR.code();
        this.message = message;
    }

    public MessageQueueException(String code, String message, Throwable cause) {
        this(code, message, cause, null);
    }

    public MessageQueueException(String code, String message, Throwable cause, Object data) {
        super(code, message, cause);
        this.data = data;
    }
}
