package com.lyloou.component.dto;

import org.springframework.cglib.beans.BeanCopier;

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
            final BeanCopier copier = BeanCopier.create(source.getClass(), dest, false);
            copier.copy(source, result, null);
            if (function != null) {
                function.apply(result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <S, T> T convert(S source, Class<T> dest) {
        return convert(source, dest, null);
    }

    public static <S, T> T convert(S source, T dest) {
        if (source == null || dest == null) {
            return null;
        }
        T result = dest;
        final BeanCopier copier = BeanCopier.create(source.getClass(), dest.getClass(), false);
        copier.copy(source, result, null);
        return result;
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> dest) {
        return convertList(source, dest, null);
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> dest, ConvertCallback<S, T> callback) {
        if (source == null) {
            return null;
        }
        return source.stream().map(s -> {
            T result = null;
            try {
                result = dest.newInstance();
                convert(s, result);
                if (callback != null) {
                    callback.callback(s, result);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return result;
        }).collect(Collectors.toList());
    }

    public interface ConvertCallback<S, D> {
        void callback(S source, D dest);
    }
}
