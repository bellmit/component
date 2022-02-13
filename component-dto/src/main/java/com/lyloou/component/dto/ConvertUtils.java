package com.lyloou.component.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 转换和属性拷贝
 *
 * @author lilou
 */
public class ConvertUtils {


    /**
     * 新建一个dest的实例，并将 source 的属性值拷贝到 dest 相同的属性上
     *
     * @param source    源实例
     * @param destClass 目标实例的类
     * @param function  转换完成后需要对 dest 实例做的额外操作
     * @param <S>       源类型
     * @param <T>       目标类型
     * @return 目标实例
     */
    public static <S, T> T convert(S source, Class<T> destClass, Function<T, T> function) {
        if (source == null) {
            return null;
        }
        try {
            T result = destClass.newInstance();
            convert(source, result);
            if (function != null) {
                function.apply(result);
            }
            return result;
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }

    /**
     * 新建一个dest的实例，并将 source 的属性值拷贝到 dest 相同的属性上
     *
     * @param source    源实例
     * @param destClass 目标实例的类
     * @param <S>       源类型
     * @param <T>       目标类型
     * @return 目标实例
     */
    public static <S, T> T convert(S source, Class<T> destClass) {
        return convert(source, destClass, null);
    }

    /**
     * 将 source 的属性值拷贝到 dest 相同的属性上
     *
     * @param source 源实例
     * @param dest   目标实例
     * @param <S>    源类型
     * @param <T>    目标类型
     * @return 目标实例
     */
    public static <S, T> T convert(S source, T dest) {
        if (source == null || dest == null) {
            return null;
        }
        BeanUtil.copyProperties(source, dest);
        return dest;
    }

    /**
     * 新建 dest 实例，并将 source 的属性值拷贝到 dest 相同的属性上，一对一形成列表
     *
     * @param sourceList 源实例列表
     * @param destClass  目标实例的类
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标实例列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> destClass) {
        return convertList(sourceList, destClass, null);
    }

    /**
     * 新建 dest 实例，并将 source 的属性值拷贝到 dest 相同的属性上，一对一形成列表
     *
     * @param sourceList 源实例列表
     * @param destClass  目标实例的类
     * @param biConsumer 额外的消费操作，可以自定义赋值
     * @param <S>        源类型
     * @param <T>        目标类型
     * @return 目标实例列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> destClass, BiConsumer<S, T> biConsumer) {
        if (sourceList == null) {
            return null;
        }
        if (sourceList.isEmpty()) {
            return Collections.emptyList();
        }

        return sourceList.stream().map(s -> {
            try {
                T result = destClass.newInstance();
                convert(s, result);
                if (biConsumer != null) {
                    biConsumer.accept(s, result);
                }
                return result;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConvertException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 将字符串类型的数据转换成指定类型的对象
     *
     * @param <T>   泛型
     * @param data  字符串数据
     * @param clazz 要转换的类型
     * @return 转换后的实例对象
     */
    @SuppressWarnings("unchecked")
    public <T> T convertStr(String data, Class<?> clazz) {
        if (String.class == clazz) {
            return (T) data;
        }

        if (StrUtil.isEmpty(data)) {
            return null;
        }

        if (Byte.class == clazz) {
            return (T) Byte.valueOf(data);
        }

        if (Character.class == clazz) {
            return (T) Character.valueOf(data.charAt(0));
        }

        if (Integer.class == clazz) {
            return (T) Integer.valueOf(data);
        }

        if (Double.class == clazz) {
            return (T) Double.valueOf(data);
        }

        if (BigDecimal.class == clazz) {
            return (T) new BigDecimal(data);
        }

        if (Float.class == clazz) {
            return (T) Float.valueOf(data);
        }

        if (Boolean.class == clazz) {
            return (T) Boolean.valueOf(data);
        }

        if (JSONObject.class == clazz) {
            return (T) JSON.parseObject(data);
        }

        if (JSONArray.class == clazz) {
            return (T) JSON.parseArray(data);
        }

        // 转换为对象
        Object o;
        try {
            o = JSON.parse(data);
        } catch (Exception e) {
            o = null;
        }
        // 是 object 对象？
        if (o instanceof JSONObject) {
            return (T) JSON.parseObject(data, clazz);
        }
        // 是 array 对象？
        if (o instanceof JSONArray) {
            return JSON.parseObject(data, new TypeReference<T>() {
            });
        }

        throw new RuntimeException("Unsupported type cast exception, class: " + clazz.getName() + ", data: " + data);
    }

    /**
     * 将字符串类型的数据转换成指定类型的对象
     *
     * @param <T>              泛型
     * @param data             字符串数据
     * @param clazz            要转换的类型
     * @param swallowException 是否吞掉异常，如果有异常直接返回 null
     * @return 转换后的实例对象
     */
    public <T> T convertStr(String data, Class<?> clazz, boolean swallowException) {
        if (swallowException) {
            try {
                return convertStr(data, clazz);
            } catch (Exception e) {
                return null;
            }
        }

        return convertStr(data, clazz);
    }


}
