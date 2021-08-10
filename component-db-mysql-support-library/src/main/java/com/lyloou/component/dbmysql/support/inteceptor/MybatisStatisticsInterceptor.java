package com.lyloou.component.dbmysql.support.inteceptor;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import com.lyloou.component.dbmysql.support.model.StatisticsKeyConstant;
import com.lyloou.component.dbmysql.support.service.MybatisStatisticsService;
import com.lyloou.component.dbmysql.support.util.HttpServletUtil;
import com.lyloou.component.dbmysql.support.util.IPUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
@Component
public class MybatisStatisticsInterceptor implements Interceptor {
    List<MybatisStatisticsService> mybatisStatisticsServices = new ArrayList<>();

    public void addMybatisStatisticServices(MybatisStatisticsService service) {
        mybatisStatisticsServices.add(service);
    }

    public void removeMybatisStatisticServices(MybatisStatisticsService service) {
        mybatisStatisticsServices.remove(service);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 执行方法
        Object result = invocation.proceed();
        if (mybatisStatisticsServices.isEmpty()) {
            return result;
        }

        doInvocation(invocation);

        return result;
    }

    private void doInvocation(Invocation invocation) {
        // 获取MapperStatement对象，获取到sql的详细信息
        Object realTarget = realTarget(invocation.getTarget());
        // 获取metaObject对象
        MetaObject metaObject = SystemMetaObject.forObject(realTarget);
        // 获取MappedStatement对象
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 获取方法的全类名称
        String methodFullName = ms.getId();

        Statement statement;
        // 获取方法参数
        Object[] args = invocation.getArgs();
        Object firstArg = args[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }

        MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);
        if (stmtMetaObj.getValue("statement") instanceof PreparedStatementProxy) {
            statement = ((PreparedStatementProxy) stmtMetaObj.getValue("statement")).getRawObject();
        } else {
            // 获取Statement对象（sql语法已经构建完毕）
            statement = (Statement) stmtMetaObj.getOriginalObject();
        }

        // 获取sql语句
        String originalSql = statement.toString();

        Map<String, String> statisticsInfoMap = getStatisticsInfoMap(methodFullName, originalSql);
        doProcess(invocation, statisticsInfoMap);
    }

    private void doProcess(Invocation invocation, Map<String, String> statisticsInfoMap) {
        for (MybatisStatisticsService service : mybatisStatisticsServices) {

            if (!service.filter(invocation, statisticsInfoMap.get(StatisticsKeyConstant.DAO_METHOD_NAME))) {
                continue;
            }

            service.process(invocation, statisticsInfoMap);
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * <p>
     * 获得真正的处理对象,可能多层代理.
     * </p>
     */
    @SuppressWarnings("unchecked")
    public static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }


    /**
     * 填充日记记录SQL参数
     */
    public static Map<String, String> getStatisticsInfoMap(String methodFullName, String originalSql) {
        Map<String, String> map = new LinkedHashMap<>();

        String unknown = "UNKNOWN";

        // 获取Request对象
        HttpServletRequest request = HttpServletUtil.getRequest();

        String uri;
        String ip;

        if (request == null) {
            uri = unknown;
            ip = unknown;
        } else {
            uri = request.getRequestURI();
            ip = IPUtils.getIpAddress(request);
        }

        // 调用的接口
        map.put(StatisticsKeyConstant.URI, uri);
        //DAO层执行的方法名称
        map.put(StatisticsKeyConstant.DAO_METHOD_NAME, methodFullName);
        // ip地址
        map.put(StatisticsKeyConstant.IP, ip);
        // 完整SQL语句
        map.put(StatisticsKeyConstant.WHOLE_SQL, handlerSql(originalSql));
        // 创建时间
        map.put(StatisticsKeyConstant.CREATE_DATE, DateUtil.formatDateTime(new Date()));
        return map;
    }

    /**
     * 处理SQL语句
     */
    private static String handlerSql(String originalSql) {
        String sql = originalSql.substring(originalSql.indexOf(":") + 1);
        // format sql
        return SQLUtils.formatMySql(sql);
    }

}
