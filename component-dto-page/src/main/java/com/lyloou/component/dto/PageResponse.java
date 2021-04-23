package com.lyloou.component.dto;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Response with batch page record to return,
 * usually use in page query
 * <p/>
 * Created by xiaochu.lbj on 2020/06/30.
 */
@SuppressWarnings("all")
public class PageResponse<T> extends Response {

    private static final long serialVersionUID = 1L;


    private PageInfo<T> data;

    public PageInfo<T> getData() {
        return data;
    }

    public void setData(PageInfo<T> data) {
        this.data = data;
    }


    public static PageResponse buildSuccess() {
        PageResponse response = new PageResponse();
        response.setSuccess(true);
        return response;
    }

    public static <T> PageResponse<T> buildSuccess(PageInfo<T> pageInfo) {
        PageResponse<T> response = new PageResponse<>();
        response.setData(pageInfo);
        response.setSuccess(true);
        return response;
    }

    public static PageResponse buildFailure(String code, String message) {
        PageResponse response = new PageResponse();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static void startPage(int pageNo, int pageSize) {
        final Page<Object> page = PageHelper.startPage(pageNo, pageSize);
    }

}
