package com.lyloou.component.notify.qywechat.model.request;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author lilou
 */
public class TextMessage implements Message {

    private String text;
    private List<String> mentionedMobileList;
    private List<String> mentionedList;
    private boolean isAtAll;

    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getMentionedMobileList() {
        return mentionedMobileList;
    }

    public void setMentionedMobileList(List<String> mentionedMobileList) {
        this.mentionedMobileList = mentionedMobileList;
    }

    public List<String> getMentionedList() {
        return mentionedList;
    }

    public void setMentionedList(List<String> mentionedList) {
        this.mentionedList = mentionedList;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }

    @Override
    public String msgType() {
        return "text";
    }

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<>(2);
        items.put("msgtype", msgType());

        Map<String, Object> textContent = new HashMap<>(3);
        if (StrUtil.isBlank(text)) {
            throw new IllegalArgumentException("text should not be blank");
        }
        textContent.put("content", text);

        if (isAtAll) {
            if (CollectionUtil.isEmpty(mentionedList)) {
                mentionedList = new ArrayList<>(1);
            }
            if (!mentionedList.contains("@all")) {
                mentionedList.add("@all");
            }
        }

        if (!CollectionUtil.isEmpty(mentionedList)) {
            textContent.put("mentioned_list", mentionedList);
        }

        if (!CollectionUtil.isEmpty(mentionedMobileList)) {
            textContent.put("mentioned_mobile_list", mentionedMobileList);
        }

        items.put("text", textContent);

        return JSON.toJSONString(items);
    }
}
