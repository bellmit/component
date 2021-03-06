package com.lyloou.component.dto;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Response with batch page record to return,
 * usually use in page query
 * <p/>
 * Created by xiaochu.lbj on 2020/06/30.
 */
@SuppressWarnings("all")
@ApiModel("返回分页数据分装")
public class PageResponse<T> extends Response {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回的数据")
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
}
