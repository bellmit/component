package com.lyloou.component.dto.field;

import java.io.Serializable;

/**
 * getter方法接口定义
 */
@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object apply(T source);
}
