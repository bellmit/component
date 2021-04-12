package com.lyloou.component.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Response with batch record to return,
 * usually use in conditional query
 * <p/>
 * Created by Danny.Lee on 2017/11/1.
 */
public class MultiResponse<T extends Collection<R>, R> extends Response {

    private static final long serialVersionUID = 1L;

    private T data;

    public List<R> getData() {
        return null == data ? Collections.emptyList() : new ArrayList<>(data);
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public static <T extends Collection<R>, R> MultiResponse<T, R> buildFailure() {
        MultiResponse<T, R> response = new MultiResponse<>();
        response.setSuccess(false);
        response.setCodeMessage(SystemCodeMessage.SYSTEM_ERROR);
        return response;
    }

    public static <T extends Collection<R>, R> MultiResponse<T, R> buildFailure(CodeMessage codeMessage) {
        MultiResponse<T, R> response = new MultiResponse<>();
        response.setSuccess(false);
        response.setCode(codeMessage.code());
        response.setMessage(codeMessage.message());
        return response;
    }

    public static <T extends Collection<R>, R> MultiResponse<T, R> buildFailure(CodeMessage codeMessage, String... messageArray) {
        MultiResponse<T, R> response = new MultiResponse<>();
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

    public static <T extends Collection<R>, R> MultiResponse<T, R> buildFailure(String code, String message) {
        MultiResponse<T, R> response = new MultiResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T extends Collection<R>, R> MultiResponse<T, R> buildSuccess(T data) {
        MultiResponse<T, R> response = new MultiResponse<>();
        response.setSuccess(true);
        response.setCodeMessage(SystemCodeMessage.SUCCESS);
        response.setData(data);
        return response;
    }

    public static <T extends Collection<R>, R> MultiResponse<T, R> buildSuccess() {
        MultiResponse<T, R> response = new MultiResponse<>();
        response.setSuccess(true);
        response.setCodeMessage(SystemCodeMessage.SUCCESS);
        return response;
    }

}
