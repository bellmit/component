package com.lyloou.component.scenarioitem.checker;

import com.lyloou.component.exceptionhandler.exception.ParamException;
import com.lyloou.component.scenarioitem.convertor.ScenarioItemConvertor;
import com.lyloou.component.scenarioitem.dto.ItemType;
import com.lyloou.component.scenarioitem.dto.ScenarioItem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/5/13
 */
public class ScenarioItemChecker {

    /**
     * 检查所有类型的map值是否符合定义（scenarioItems）
     * 1. 是否存在
     * 2. 是否可空
     *
     * @param scenarioItems 类型条目数组
     * @param configMap     map值
     */
    public static void checkConfigMap(ScenarioItem[] scenarioItems,
                                      Map<String, Map<String, Object>> configMap) {
        final List<ItemType> itemTypeList = Arrays.stream(scenarioItems)
                .map(ScenarioItem::getItemType)
                .collect(Collectors.toList());
        for (ItemType type : itemTypeList) {
            checkConfigMap(type, scenarioItems, configMap);
        }
    }

    /**
     * 检查指定类型的map值是否符合定义（scenarioItems）
     * 1. 是否存在
     * 2. 是否可空
     *
     * @param itemType      类型
     * @param scenarioItems 类型条目数组
     * @param configMap     map值
     */
    public static void checkConfigMap(ItemType itemType,
                                      ScenarioItem[] scenarioItems,
                                      Map<String, Map<String, Object>> configMap) {
        final Map<String, Object> itemKeyValueMap = ScenarioItemConvertor.getItemKeyValueMap(itemType, configMap);

        final List<ScenarioItem> scenarioItemList = Arrays.stream(scenarioItems)
                .filter(scenarioItem -> scenarioItem.getItemType() == itemType)
                .collect(Collectors.toList());

        for (ScenarioItem scenarioItem : scenarioItemList) {
            checkTypeItemValueNullable(scenarioItem, itemKeyValueMap);
        }
    }

    /**
     * 检查是否可空
     *
     * @param scenarioItem    类型条目对象
     * @param itemKeyValueMap map值
     */
    private static void checkTypeItemValueNullable(ScenarioItem scenarioItem, Map<String, Object> itemKeyValueMap) {
        final Object itemValue = scenarioItem.getValue(itemKeyValueMap);
        final Boolean required = scenarioItem.isRequired();
        if (required != null && required) {
            if (itemValue == null) {
                final String message = String.format("必填参数为空，itemType:%s,itemKey:%s",
                        scenarioItem.getItemType().getName(),
                        scenarioItem.getItemKey()
                );
                throw new ParamException(message);
            }
        }
    }
}
