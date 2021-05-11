package com.lyloou.component.notify.qywechat.model.response;

/**
 * @author lilou
 * @since 2021/5/11
 */
public class QyWechatResponse {
    private Integer errcode;
    private String errmsg;
    private String type;
    private String mediaId;
    private String createdAt;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", type='" + type + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
