package com.lyloou.component.dbmysql.support.service;

import cn.hutool.json.JSONUtil;
import com.lyloou.component.dbmysql.support.inteceptor.MybatisStatisticsInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author lilou
 * @since 2021/8/10
 */
@Slf4j
public class MybatisStatisticsLogRecordServiceImpl implements MybatisStatisticsService {
    private List<String> excludedPrefixes;

    @Autowired
    private MybatisStatisticsInterceptor interceptor;

    @PostConstruct
    public void init() {
        interceptor.addMybatisStatisticServices(this);
    }

    @Override
    public boolean filter(Invocation invocation, String daoMethodName) {
        if (CollectionUtils.isEmpty(excludedPrefixes)) {
            return true;
        }
        for (String prefix : excludedPrefixes) {
            if (daoMethodName.startsWith(prefix)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void process(Invocation invocation, Map<String, String> statisticsInfoMap) {
        log.info("Mybatis statistics log record:" +
                "\n===============================" +
                "\n{}" +
                "\n===============================", JSONUtil.toJsonPrettyStr(statisticsInfoMap));
    }

    public List<String> getExcludedPrefixes() {
        return excludedPrefixes;
    }

    public void setExcludedPrefixes(List<String> excludedPrefixes) {
        this.excludedPrefixes = excludedPrefixes;
    }
}
