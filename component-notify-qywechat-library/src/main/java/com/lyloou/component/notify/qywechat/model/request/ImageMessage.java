package com.lyloou.component.notify.qywechat.model.request;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lyloou
 */
public class ImageMessage implements Message {

    private final String base64;
    private final String md5;

    public ImageMessage(String base64, String md5) {
        this.base64 = base64;
        this.md5 = md5;
    }

    @Override
    public String msgType() {
        return "image";
    }

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", msgType());

        Map<String, Object> textContent = new HashMap<String, Object>();
        if (StrUtil.isEmpty(base64)) {
            throw new IllegalArgumentException("base64 should not be blank");
        }

        textContent.put("base64", base64);
        textContent.put("md5", md5);
        items.put("image", textContent);
        return JSONUtil.toJsonStr(items);
    }
}
