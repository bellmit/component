package com.lyloou.component.dto;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

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

    public static <T> PageResponse<T> buildSuccess(List<T> dataList) {
        PageResponse<T> response = new PageResponse<>();
        final PageInfo<T> pageInfo = new PageInfo<T>(dataList);
        response.setData(pageInfo);
        response.setSuccess(true);
        return response;
    }

    /**
     * 转换数据list为带分页格式，并替换数据为viewlist
     *
     * @param sourceDataList 数据库对应list
     * @param viewList       展现层list
     * @return 带分页格式的展现层list
     */
    public static <T> PageResponse<T> buildSuccess(List sourceDataList, List<T> viewList) {
        PageResponse<T> response = new PageResponse<>();
        final PageInfo pageInfo = new PageInfo(sourceDataList);
        pageInfo.setList(viewList);
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
