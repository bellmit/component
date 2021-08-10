package com.lyloou.component.dbmysql.support.service;

import org.apache.ibatis.plugin.Invocation;

import java.util.Map;

/**
 * @author lilou
 * @since 2021/8/10
 */
public interface MybatisStatisticsService {

    /**
     * 处理日志信息
     *
     * @param invocation        拦截的对象
     * @param statisticsInfoMap 日志信息map集合
     */
    void process(Invocation invocation, Map<String, String> statisticsInfoMap);

    /**
     * 过滤器
     *
     * @param invocation    拦截对象
     * @param daoMethodName dao方法名称
     * @return 结果
     */
    default boolean filter(Invocation invocation, String daoMethodName) {
        return true;
    }
}
