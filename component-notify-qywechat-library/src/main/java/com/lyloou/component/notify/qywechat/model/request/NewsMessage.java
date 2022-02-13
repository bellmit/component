package com.lyloou.component.notify.qywechat.model.request;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图文消息
 *
 * @author lyloou
 */
public class NewsMessage implements Message {
    public static final int MAX_ARTICLE_CNT = 8;
    public static final int MIN_ARTICLE_CNT = 1;

    private final List<NewsArticle> articles = new ArrayList<NewsArticle>();

    public void addNewsArticle(NewsArticle newsArticle) {
        if (articles.size() >= MAX_ARTICLE_CNT) {
            return;
        }
        articles.add(newsArticle);
    }

    @Override
    public String msgType() {
        return "news";
    }

    @Override
    public String toJsonString() {
        Map<String, Object> items = new HashMap<>(2);
        items.put("msgtype", msgType());

        Map<String, Object> news = new HashMap<>(1);
        if (articles.size() < MIN_ARTICLE_CNT) {
            throw new IllegalArgumentException("number of articles can't less than " + MIN_ARTICLE_CNT);
        }
        news.put("articles", articles);
        items.put("news", news);
        return JSON.toJSONString(items);
    }

    public static class NewsArticle {
        private String title;
        private String description;
        private String picurl;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}
