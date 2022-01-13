package com.lyloou.component.dto.field;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * 属性工具类，用来获取 Getter 和 Setter 属性的名称。支持首字母小写样式，下划线的样式和自定义样式
 * <p>
 * 参考：[利用Lambda实现通过getter/setter方法引用拿到属性名 - SegmentFault 思否](https://segmentfault.com/a/1190000019389160)
 *
 * @author lilou
 */
public class FieldUtil {

    /*
     * ===========> getter 方法引用 <===========
     */

    /**
     * 下划线样式，小写
     */
    public static <T> String underline(IGetter<T> fn) {
        return toSymbolCase(fn, '_');
    }

    /**
     * 下划线样式，大写
     */
    public static <T> String underlineUpper(IGetter<T> fn) {
        return underline(fn).toUpperCase();
    }

    /**
     * 依据符号转换样式
     */
    public static <T> String toSymbolCase(IGetter<T> fn, char symbol) {
        return StrUtil.toSymbolCase(noPrefix(fn), symbol);
    }

    /***
     * 转换getter方法引用为属性名，首字母小写
     */
    public static <T> String noPrefix(IGetter<T> fn) {
        return getGeneralField(fn);
    }

    /*
     * ===========> setter 方法引用 <===========
     */

    /**
     * 下划线样式，小写
     */
    public static <T, R> String underline(ISetter<T, R> fn) {
        return toSymbolCase(fn, '_');
    }

    /**
     * 下划线样式，大写
     */
    public static <T, R> String underlineUpper(ISetter<T, R> fn) {
        return underline(fn).toUpperCase();
    }

    /**
     * 依据符号转换样式
     */
    public static <T, R> String toSymbolCase(ISetter<T, R> fn, char symbol) {
        return StrUtil.toSymbolCase(noPrefix(fn), symbol);
    }

    /**
     * 转换setter方法引用为属性名，首字母小写
     */
    public static <T, R> String noPrefix(ISetter<T, R> fn) {
        return getGeneralField(fn);
    }


    /*
     * ===========> 复用功能 <===========
     */

    /**
     * 获得set或get或is方法对应的标准属性名，其它前缀的方法名使用原名
     */
    private static String getGeneralField(Serializable fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String getOrSetMethodName = lambda.getImplMethodName();
        final String generalField = StrUtil.getGeneralField(getOrSetMethodName);
        return StrUtil.isEmpty(generalField) ? getOrSetMethodName : generalField;
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

    /**
     * getter方法接口定义
     */
    @FunctionalInterface
    public interface IGetter<T> extends Serializable {
        Object apply(T source);
    }

    /**
     * setter方法接口定义
     */
    @FunctionalInterface
    public interface ISetter<T, U> extends Serializable {
        void accept(T t, U u);
    }
}