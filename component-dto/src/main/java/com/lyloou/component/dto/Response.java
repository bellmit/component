package com.lyloou.component.dto;

import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;
import io.swagger.annotations.ApiModelProperty;

/**
 * Response to caller
 *
 * @author fulan.zjf 2017年10月21日 下午8:53:17
 */
public class Response extends DTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回结果")
    private boolean success;

    @ApiModelProperty("返回码")
    private String code;

    @ApiModelProperty("请求ID")
    private String requestId;

    @ApiModelProperty("返回消息")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setCodeMessage(CodeMessage codeMessage) {
        setCode(codeMessage.code());
        setMessage(codeMessage.message());
    }

    @Override
    public String toString() {
        return "Response [success=" + success + ", code=" + code + ", message=" + message + "]";
    }

    public static Response success() {
        Response response = new Response();
        response.setSuccess(true);
        response.setCode(CommonCodeMessage.SUCCESS.code());
        response.setMessage(CommonCodeMessage.SUCCESS.message());
        return response;
    }

    public static Response failure(String code, String message) {
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }


    public static Response failure(CodeMessage codeMessage) {
        Response response = new Response();
        response.setSuccess(false);
        response.setCodeMessage(codeMessage);
        return response;
    }
}
