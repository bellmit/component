package com.lyloou.component.notify.qywechat;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.lyloou.component.notify.qywechat.model.request.Message;
import com.lyloou.component.notify.qywechat.model.response.QyWechatResponse;

/**
 * @author lilou
 * @since 2021/5/11
 */
public class QyWechatRobotClient {

    /**
     * 企业微信机器人发送消息，默认超时12000毫秒
     *
     * @param webhookUrl 钩子链接
     * @param message    消息体
     * @return 结果
     */
    public static QyWechatResponse send(String webhookUrl, Message message) {
        return send(webhookUrl, message, 12000);
    }

    /**
     * 企业微信机器人发送消息
     *
     * @param webhookUrl 钩子链接
     * @param message    消息体
     * @param timeout    超时（毫秒）
     * @return 结果
     */
    public static QyWechatResponse send(String webhookUrl, Message message, int timeout) {
        final String result = HttpUtil.post(webhookUrl, message.toJsonString(), timeout);
        return JSON.parseObject(result, QyWechatResponse.class);
    }
}
