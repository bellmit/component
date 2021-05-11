package com.lyloou.component.notify.qywechat.model.request;

/**
 * @author lilou
 */
public interface Message {

    /**
     * 消息类型
     *
     * @return 结果
     */
    String msgType();

    /**
     * 返回消息的Json格式字符串
     *
     * @return 消息的Json格式字符串
     */
    String toJsonString();
}
