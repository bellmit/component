package com.lyloou.component.dto;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lilou
 * @since 2021/4/23
 */
public class PageInfoHelper {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> PageInfo<T> getPageInfo(List sourceDataList, List<T> viewList) {
        final PageInfo pageInfo = new PageInfo(sourceDataList);
        pageInfo.setList(viewList);
        return pageInfo;
    }

    public static <T> PageInfo<T> getPageInfo(List<T> dataList) {
        return new PageInfo<>(dataList);
    }


}
