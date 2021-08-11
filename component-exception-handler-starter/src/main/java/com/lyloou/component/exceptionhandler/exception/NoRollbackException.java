package com.lyloou.component.exceptionhandler.exception;

import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;

/**
 * 无须回滚异常
 * 示例：
 * <code>
 *
 * @Transactional(rollbackFor = Exception.class, noRollbackFor = NoRollbackException.class)
 * public void payOrder(String orderInfoStr) {
 * }
 * </code>
 * @since 2021/8/11
 */
public class NoRollbackException extends CommonException {

    public NoRollbackException() {
        this.code = CommonCodeMessage.DB_NO_ROLLBACK_ERROR.code();
        this.message = CommonCodeMessage.DB_NO_ROLLBACK_ERROR.message();
    }

    public NoRollbackException(CodeMessage codeMessage) {
        super(codeMessage.code(), codeMessage.message());
        this.code = codeMessage.code();
        this.message = codeMessage.message();
    }

    public NoRollbackException(String message) {
        super(message);
        this.code = CommonCodeMessage.DB_NO_ROLLBACK_ERROR.code();
        this.message = message;
    }

    public NoRollbackException(String code, String message) {
        super(code, message);
        this.code = code;
        this.message = message;
    }

    public NoRollbackException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonCodeMessage.DB_NO_ROLLBACK_ERROR.code();
        this.message = message;
    }

    public NoRollbackException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
