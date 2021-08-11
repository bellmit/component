package com.lyloou.component.dto;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lilou
 * @since 2021/4/23
 */
public class PageInfoHelper {

    public static <E> Page<E> startPage(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize);
    }

    /**
     * 获取分页信息
     *
     * @param sourceDataList 查询数据库的列表（不能随意改其他，否则计数不准）
     * @param viewList       要显示的列表
     * @param <T>            泛型
     * @return 结果
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> PageInfo<T> getPageInfo(List sourceDataList, List<T> viewList) {
        final PageInfo pageInfo = new PageInfo(sourceDataList);
        pageInfo.setList(viewList);
        return pageInfo;
    }

    /**
     * 获取分页信息
     *
     * @param dataList 查询的数据和显示的数据是同一个
     * @param <T>      泛型
     * @return 结果
     */
    public static <T> PageInfo<T> getPageInfo(List<T> dataList) {
        return getPageInfo(dataList, dataList);
    }


}
