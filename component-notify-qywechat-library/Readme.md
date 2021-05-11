## component-notify-qywechat-library

描述：通知组件库-企业微信

使用方法：

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-notify-qywechat-library</artifactId>
    <version>${project.version}</version>
</dependency>
```

2. 使用

参考实例： `com.lyloou.component.notify.qywechat.ClientTest`

```java
class Main {
    private static void sendText() {
        TextMessage message = new TextMessage("你是我的小苹果");
        message.setAtAll(true);
        sendAndPrint(message);
    }

    private static void sendAndPrint(Message message) {
        final QyWechatResponse response = QyWechatRobotClient.send(CHATBOT_WEBHOOK, message);
        System.out.println(response);
    }
}
```

3. 参考资料

- https://github.com/yanceyZhang/wx-chatbot
- https://work.weixin.qq.com/api/doc/90000/90136/91770
