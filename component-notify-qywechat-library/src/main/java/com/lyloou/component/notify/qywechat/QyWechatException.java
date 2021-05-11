package com.lyloou.component.notify.qywechat;

/**
 * @author lilou
 * @since 2021/5/11
 */
public class QyWechatException extends RuntimeException {
    public QyWechatException() {
    }

    public QyWechatException(String message) {
        super(message);
    }

    public QyWechatException(String message, Throwable cause) {
        super(message, cause);
    }

    public QyWechatException(Throwable cause) {
        super(cause);
    }

    public QyWechatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
