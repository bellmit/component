package com.lyloou.component.domain;

public class DomainFactory {

    public static <T> T create(Class<T> entityClz) {
        return DomainApplicationContextHelper.getBean(entityClz);
    }

}
