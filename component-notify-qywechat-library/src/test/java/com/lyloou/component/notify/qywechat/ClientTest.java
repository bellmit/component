package com.lyloou.component.notify.qywechat;

import com.lyloou.component.notify.qywechat.model.request.*;
import com.lyloou.component.notify.qywechat.model.response.QyWechatResponse;
import com.lyloou.component.notify.qywechat.util.Base64Utils;

import java.util.ArrayList;

/**
 * @author lilou
 * @since 2021/5/11
 */
public class ClientTest {
    public static final String CHATBOT_WEBHOOK = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=bdbe32a6-cdb6-4fa0-807a-11111111111";

    public static void main(String[] args) {
        sendSplit();
        sendImage();

        sendSplit();
        sendMarkdown();

        sendSplit();
        sendText();

        sendSplit();
        sendNews();
    }

    private static void sendNews() {
        NewsMessage message = new NewsMessage();
        for (int i = 0; i < 10; i++) {
            final NewsMessage.NewsArticle newsArticle = new NewsMessage.NewsArticle();
            newsArticle.setTitle("文章-" + i);
            newsArticle.setDescription("描述-" + i);
            newsArticle.setUrl("http://ip138.com");
            newsArticle.setPicurl("https://c-ssl.duitang.com/uploads/blog/202008/22/20200822114634_2b3e0.thumb.1000_0.png");
            message.addNewsArticle(newsArticle);
        }
        sendAndPrint(message);
    }

    private static void sendAndPrint(Message message) {
        final QyWechatResponse response = QyWechatRobotClient.send(CHATBOT_WEBHOOK, message);
        System.out.println(response);
    }

    private static void sendSplit() {
        TextMessage message = new TextMessage("=============分隔线=============");
        final ArrayList<String> mentionedMobileList = new ArrayList<>();
        sendAndPrint(message);
    }

    private static void sendText() {
        TextMessage message = new TextMessage("你是我的小苹果");
        final ArrayList<String> mentionedMobileList = new ArrayList<>();
        mentionedMobileList.add("15290831111");
        message.setMentionedMobileList(mentionedMobileList);
        message.setAtAll(false);
        sendAndPrint(message);
    }

    private static void sendMarkdown() {

        MarkdownMessage message = new MarkdownMessage();
        message.add(MarkdownMessage.Render.boldText("Hello Bold"));
        message.add(MarkdownMessage.Render.italicText("Hello Italic"));
        message.add(MarkdownMessage.Render.imageText("https://c-ssl.duitang.com/uploads/blog/202008/22/20200822114634_2b3e0.thumb.1000_0.png"));
        message.add(MarkdownMessage.Render.linkText("Hello Link", "http://ip138.com"));
        final ArrayList<String> orderItem = new ArrayList<>();
        orderItem.add("first 1");
        orderItem.add("second 2");
        orderItem.add("three 3");
        message.add(MarkdownMessage.Render.orderListText(orderItem));
        message.add("实时新增用户反馈<font color=\\\"warning\\\">132例</font>，请相关同事注意。\n" +
                "         >类型:<font color=\\\"comment\\\">用户反馈</font>\n" +
                "         >普通用户反馈:<font color=\\\"comment\\\">117例</font>\n" +
                "         >VIP用户反馈:<font color=\\\"comment\\\">15例</font>");

        sendAndPrint(message);
    }

    private static void sendImage() {
        String url = "https://c-ssl.duitang.com/uploads/blog/202008/22/20200822114634_2b3e0.thumb.1000_0.png";
        Base64Utils.ImageBase64Md5 image = Base64Utils.imageToBase64ByOnline(url);
        ImageMessage message = new ImageMessage(image.getBase64(), image.getMd5());

        sendAndPrint(message);
    }
}
