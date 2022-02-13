package com.lyloou.component.notify.qywechat.model.request;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lilou
 */
public class MarkdownMessage implements Message {

    private final List<String> items = new ArrayList<String>();

    public static class Render {
        public static String boldText(String text) {
            return "**" + text + "**";
        }

        public static String italicText(String text) {
            return "*" + text + "*";
        }

        public static String linkText(String text, String href) {
            return "[" + text + "](" + href + ")";
        }

        public static String imageText(String imageUrl) {
            return "![image](" + imageUrl + ")";
        }

        public static String headerText(int headerType, String text) {
            if (headerType < 1 || headerType > 6) {
                throw new IllegalArgumentException("headerType should be in [1, 6]");
            }

            StringBuilder numbers = new StringBuilder();
            for (int i = 0; i < headerType; i++) {
                numbers.append("#");
            }
            return numbers + " " + text;
        }

        public static String referenceText(String text) {
            return "> " + text;
        }

        public static String orderListText(List<String> orderItem) {
            if (orderItem.isEmpty()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= orderItem.size() - 1; i++) {
                sb.append(i).append(". ").append(orderItem.get(i - 1)).append("\n");
            }
            sb.append(orderItem.size()).append(". ").append(orderItem.get(orderItem.size() - 1));
            return sb.toString();
        }

        public static String getUnorderListText(List<String> unorderItem) {
            if (unorderItem.isEmpty()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < unorderItem.size() - 1; i++) {
                sb.append("- ").append(unorderItem.get(i)).append("\n");
            }
            sb.append("- ").append(unorderItem.get(unorderItem.size() - 1));
            return sb.toString();
        }
    }


    public void add(String text) {
        items.add(text);
    }

    @Override
    public String msgType() {
        return "markdown";
    }

    @Override
    public String toJsonString() {
        Map<String, Object> result = new HashMap<>(2);
        result.put("msgtype", msgType());

        Map<String, Object> markdown = new HashMap<>(1);

        StringBuilder markdownText = new StringBuilder();
        for (String item : items) {
            markdownText.append(item).append("\n");
        }
        markdown.put("content", markdownText.toString());
        result.put("markdown", markdown);

        return JSON.toJSONString(result);
    }
}
