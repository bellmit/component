package com.lyloou.component.exceptionhandler.model;

/**
 * 根据不同的错误级别，可以分别记录和处理
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum ErrorLevel {
    NONE,
    INFO,
    WARN,
    ERROR
}