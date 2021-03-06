package com.lyloou.component.dbmysqlseata.support;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.common.util.StringUtils;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Component;

/**
 * @author: ma wei long
 * @date: 2020年7月28日 上午12:28:08
 */
@Component
public class RequestHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String xid = RootContext.getXID();
        if (StringUtils.isNotBlank(xid)) {
            template.header("Fescar-Xid", xid);
        }
    }
}