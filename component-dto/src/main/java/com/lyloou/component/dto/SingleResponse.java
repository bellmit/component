package com.lyloou.component.dto;

import com.lyloou.component.dto.codemessage.CodeMessage;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Response with single record to return
 * <p/>
 * Created by Danny.Lee on 2017/11/1.
 */
@ApiModel("返回单对象数据分装")
public class SingleResponse<T> extends Response {

    @ApiModelProperty("返回的数据")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> SingleResponse<T> buildFailure() {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(false);
        response.setCodeMessage(CommonCodeMessage.ERROR);
        return response;
    }

    public static <T> SingleResponse<T> buildFailure(CodeMessage codeMessage) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(false);
        response.setCode(codeMessage.code());
        response.setMessage(codeMessage.message());
        return response;
    }

    public static <T> SingleResponse<T> buildFailure(CodeMessage codeMessage, String... messageArray) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(false);
        response.setCode(codeMessage.code());

        String message = codeMessage.message();
        if (messageArray != null) {
            for (String msg : messageArray) {
                message = message.concat(" ").concat(msg);
            }
        }
        response.setMessage(message);
        return response;
    }

    public static <T> SingleResponse<T> buildFailure(String message) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(false);
        response.setCode(CommonCodeMessage.ERROR.code());
        response.setMessage(message);
        return response;
    }

    public static <T> SingleResponse<T> buildFailure(String code, String message) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> SingleResponse<T> buildSuccess(T data) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(true);
        response.setCodeMessage(CommonCodeMessage.SUCCESS);
        response.setData(data);
        return response;
    }

    public static <T> SingleResponse<T> buildSuccess() {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(true);
        response.setCodeMessage(CommonCodeMessage.SUCCESS);
        return response;
    }
}
