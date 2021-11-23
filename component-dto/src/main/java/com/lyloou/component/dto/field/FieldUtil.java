package com.lyloou.component.dto.field;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * [利用Lambda实现通过getter/setter方法引用拿到属性名 - SegmentFault 思否](https://segmentfault.com/a/1190000019389160)
 *
 * @author lilou
 */
@Slf4j
public class FieldUtil {

    public static final String PREFIX_GET = "get";
    public static final String PREFIX_IS = "is";
    public static final String PREFIX_SET = "set";

    /***
     * 转换方法引用为属性名，是否为下划线方式
     */
    public static <T> String name(IGetter<T> fn, boolean underline) {
        if (underline) {
            return StrUtil.toUnderlineCase(name(fn));
        }
        return name(fn);
    }

    public static <T> String name(IGetter<T> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith(PREFIX_GET)) {
            prefix = PREFIX_GET;
        } else if (methodName.startsWith(PREFIX_IS)) {
            prefix = PREFIX_IS;
        }
        if (prefix == null) {
            throw new IllegalArgumentException("无效的getter方法: " + methodName);
        }
        return StrUtil.lowerFirst(StrUtil.subAfter(methodName, prefix, false));
    }

    /***
     * 转换setter方法引用为属性名
     */
    public static <T, R> String name(ISetter<T, R> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        if (!methodName.startsWith(PREFIX_SET)) {
            throw new IllegalArgumentException("无效的setter方法: " + methodName);
        }
        // 截取set之后的字符串并转换首字母为小写
        return StrUtil.lowerFirst(StrUtil.subAfter(methodName, PREFIX_SET, false));
    }

    /***
     * 获取类对应的Lambda
     */
    private static SerializedLambda getSerializedLambda(Serializable fn) {
        //先检查缓存中是否已存在
        SerializedLambda lambda;
        try {

            //提取SerializedLambda并缓存
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            lambda = (SerializedLambda) method.invoke(fn);
        } catch (Exception e) {
            throw new IllegalArgumentException("获取SerializedLambda异常, class=" + fn.getClass().getSimpleName(), e);
        }
        return lambda;
    }
}