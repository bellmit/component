package com.lyloou.component.scenarioitem.dto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/4/28
 */
public interface ScenarioItem {
    /**
     * 获取类型
     *
     * @return 类型
     */
    ItemType getItemType();

    /**
     * key值
     *
     * @return 名称
     */
    String getItemKey();

    /**
     * 存储的值类型
     *
     * @return 值类型
     */
    Class<?> getItemValueType();

    /**
     * 获取描述信息
     *
     * @return 描述信息
     */
    String getDescription();

    /**
     * 是否必填
     *
     * @return 结果
     */
    Boolean isRequired();

    /**
     * 从map中获取key对应的值，并根据 type 做转换，可能为空
     *
     * @param itemKeyValueMap map集合
     * @param <T>             泛型
     * @return 结果
     */
    default <T> T getValue(Map<String, Object> itemKeyValueMap) {
        final Class<?> type = getItemValueType();
        Object obj = itemKeyValueMap.get(getItemKey());
        if (obj == null) {
            return null;
        }

        if (type == Double.class) {
            obj = Double.parseDouble(String.valueOf(obj));
        } else if (type == Integer.class) {
            obj = Integer.parseInt(String.valueOf(obj));
        } else if (type == Long.class) {
            obj = Long.parseLong(String.valueOf(obj));
        } else if (type == Boolean.class) {
            obj = Boolean.parseBoolean(String.valueOf(obj));
        } else if (type == String.class) {
            obj = String.valueOf(obj);
        } else {
            throw new RuntimeException("无效的数据类型，只支持：[Double.class, Integer.class, Long.class, Boolean.class, String.class]");
        }

        //noinspection unchecked
        return (T) type.cast(obj);
    }

    /**
     * 从map中获取key对应的值，并根据 type 做转换，不能为空，为空时会报错
     *
     * @param itemKeyValueMap map集合
     * @param <T>             泛型
     * @return 结果
     */
    default <T> T notNullValue(Map<String, Object> itemKeyValueMap) {
        return notNullValue(itemKeyValueMap, String.format("%s的值不能为空", getItemKey()));
    }

    /**
     * 从map中获取key对应的值，并根据 type 做转换，不能为空，为空时会报错
     *
     * @param itemKeyValueMap map集合
     * @param <T>             泛型
     * @param msg             自定义的错误信息
     * @return 结果
     */
    default <T> T notNullValue(Map<String, Object> itemKeyValueMap, String msg) {
        final T value = getValue(itemKeyValueMap);
        return Objects.requireNonNull(value, msg);
    }

    /**
     * 获取类型条目详情，结果按 typeEnum 来分组
     *
     * @param typeItems 类型条目
     * @return 结果
     */
    static Map<ItemType, List<Map<String, Object>>> getItemTypeToScenarioItemDocJsonMap(ScenarioItem[] typeItems) {
        final List<ItemType> typeEnums = Arrays.stream(typeItems)
                .map(ScenarioItem::getItemType)
                .distinct()
                .collect(Collectors.toList());

        Map<ItemType, List<Map<String, Object>>> resultMap = new HashMap<>(typeEnums.size());
        for (ItemType anEnum : typeEnums) {
            resultMap.put(anEnum, getScenarioItemDocJson(anEnum, typeItems));
        }

        return resultMap;
    }

    static Map<ItemType, Map<String, Object>> getItemTypeToScenarioItemSampleJsonMap(ScenarioItem[] typeItems) {
        final List<ItemType> typeEnums = Arrays.stream(typeItems)
                .map(ScenarioItem::getItemType)
                .distinct()
                .collect(Collectors.toList());

        Map<ItemType, Map<String, Object>> resultMap = new HashMap<>(typeEnums.size());
        for (ItemType itemType : typeEnums) {
            resultMap.put(itemType, getScenarioItemSampleJson(itemType, typeItems));
        }

        return resultMap;
    }

    static Map<String, Object> getScenarioItemSampleJson(ItemType itemType, ScenarioItem[] typeItems) {
        final List<ScenarioItem> list = Arrays.stream(typeItems)
                .filter(scanbuyConfigEnums -> scanbuyConfigEnums.getItemType().equals(itemType))
                .collect(Collectors.toList());

        Map<String, Object> map = new LinkedHashMap<>(5);
        for (ScenarioItem item : list) {
            Object value;
            if (isNumberType(item.getItemValueType())) {
                value = 101;
            } else {
                value = "I am value";
            }
            map.put(item.getItemKey(), value);
        }
        return map;
    }

    static boolean isNumberType(Class type) {
        return type == Long.TYPE || type == Double.TYPE || type == Byte.TYPE || type == Short.TYPE || type == Integer.TYPE || type == Float.TYPE || Number.class.isAssignableFrom(type);
    }

    /**
     * 获取类型条目详情
     *
     * @param itemType  类型
     * @param typeItems 类型条目数组
     * @return 返回配置信息
     */
    static List<Map<String, Object>> getScenarioItemDocJson(ItemType itemType, ScenarioItem[] typeItems) {
        final List<ScenarioItem> list = Arrays.stream(typeItems)
                .filter(scanbuyConfigEnums -> scanbuyConfigEnums.getItemType().equals(itemType))
                .collect(Collectors.toList());

        List<Map<String, Object>> array = new LinkedList<>();
        for (ScenarioItem item : list) {
            Map<String, Object> map = new LinkedHashMap<>(5);
            map.put("item_type", itemType.getName());
            map.put("item_key", item.getItemKey());
            map.put("item_value_type", item.getItemValueType().getName());
            map.put("is_required", item.isRequired());
            map.put("description", item.getDescription());
            array.add(map);
        }
        return array;
    }
}
