package com.lyloou.component.dto;

/**
 * Response with single record to return
 * <p/>
 * Created by Danny.Lee on 2017/11/1.
 */
public class SingleResponse<T> extends Response {

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
        response.setCodeMessage(SystemCodeMessage.SYSTEM_ERROR);
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
        response.setCode(SystemCodeMessage.SYSTEM_ERROR.code());
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
        response.setCodeMessage(SystemCodeMessage.SUCCESS);
        response.setData(data);
        return response;
    }

    public static <T> SingleResponse<T> buildSuccess() {
        SingleResponse<T> response = new SingleResponse<>();
        response.setSuccess(true);
        response.setCodeMessage(SystemCodeMessage.SUCCESS);
        return response;
    }
}
