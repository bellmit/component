package com.lyloou.component.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.ConvertException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对象拷贝
 *
 * @author lilou
 */
public class ConvertUtils {


    public static <S, T> T convert(S source, Class<T> dest, Function<T, T> function) {
        if (source == null) {
            return null;
        }
        try {
            T result = dest.newInstance();
            convert(source, result);
            if (function != null) {
                function.apply(result);
            }
            return result;
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }

    public static <S, T> T convert(S source, Class<T> dest) {
        return convert(source, dest, null);
    }

    public static <S, T> T convert(S source, T dest) {
        if (source == null || dest == null) {
            return null;
        }
        BeanUtil.copyProperties(source, dest);
        return dest;
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> dest) {
        return convertList(source, dest, null);
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> dest, ConvertCallback<S, T> callback) {
        if (source == null) {
            return null;
        }
        return source.stream().map(s -> {
            try {
                T result = dest.newInstance();
                convert(s, result);
                if (callback != null) {
                    callback.callback(s, result);
                }
                return result;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConvertException(e);
            }
        }).collect(Collectors.toList());
    }

    public interface ConvertCallback<S, D> {
        /**
         * 回调方法
         *
         * @param source 源
         * @param dest   目标
         */
        void callback(S source, D dest);
    }
}
