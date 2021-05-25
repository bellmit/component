package com.lyloou.component.scenarioitem.convertor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.lyloou.component.scenarioitem.dto.ItemType;
import com.lyloou.component.scenarioitem.dto.ScenarioItem;
import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/5/13
 */
public class ScenarioItemConvertor {

    /**
     * 示例：
     // @formatter:off
       <code>
      {
          "configMap":{
              "common":{

              }
              "lottery":{

              }
          }
      }
      </code>
     // @formatter:on
     * @param scenarioItemEntityList 列表
     * @return map集合
     */
    public static Map<String, Map<String, Object>> toConfigMap(List<ScenarioItemEntity> scenarioItemEntityList) {
        final Map<String, List<ScenarioItemEntity>> listMap = scenarioItemEntityList
                .stream()
                .collect(Collectors.groupingBy(ScenarioItemEntity::getItemType));

        Map<String, Map<String, Object>> result = new HashMap<>(listMap.size());
        listMap.forEach((type, itemEntityList) -> result.put(type, getItemKeyValueMap(itemEntityList)));
        return result;
    }

    private static Map<String, Object> getItemKeyValueMap(List<ScenarioItemEntity> itemEntityList) {
        Map<String, Object> map = new HashMap<>(itemEntityList.size());
        for (ScenarioItemEntity itemEntity : itemEntityList) {
            map.put(itemEntity.getItemKey(), itemEntity.getItemValue());
        }
        return map;
    }

    /**
     * 示例：
     // @formatter:off
     <code>

        "common":{

        }
     </code>
     // @formatter:on
     * @param itemType 类型
     * @param configMap configMap集合
     * @return itemKeyValueMap集合
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getItemKeyValueMap(ItemType itemType, Map<String, Map<String, Object>> configMap) {
        final Object itemTypeValue = configMap.get(itemType.getName());
        if (itemTypeValue == null) {
            return Collections.emptyMap();
        }
        //noinspection rawtypes
        return (Map) itemTypeValue;
    }

    public static List<ScenarioItemEntity> toScenarioItemEntityList(
            ScenarioItem[] scenarioItems,
            Map<String, Map<String, Object>> configMap) {
        return toScenarioItemCoList(scenarioItems, configMap)
                .stream()
                .map(ScenarioItemConvertor::toItemEntity)
                .collect(Collectors.toList());
    }

    public static List<ScenarioItemCO> toScenarioItemCoList(ScenarioItem[] scenarioItems,
                                                            Map<String, Map<String, Object>> configMap) {
        final List<ItemType> typeEnums = Arrays.stream(scenarioItems)
                .map(ScenarioItem::getItemType)
                .distinct()
                .collect(Collectors.toList());
        List<ScenarioItemCO> allItemList = new ArrayList<>();
        for (ItemType itemType : typeEnums) {
            final List<ScenarioItem> itemList = Arrays.stream(scenarioItems)
                    .filter(item -> item.getItemType() == itemType)
                    .collect(Collectors.toList());
            final List<ScenarioItemCO> itemCoList = getScenarioItemCoList(itemType, itemList, configMap);
            allItemList.addAll(itemCoList);
        }
        return allItemList;
    }

    public static Map<ItemType, List<ScenarioItemCO>> toScenarioItemListMap(ScenarioItem[] scenarioItems,
                                                                            Map<String, Map<String, Object>> configMap) {
        final List<ItemType> typeEnums = Arrays.stream(scenarioItems)
                .map(ScenarioItem::getItemType)
                .distinct()
                .collect(Collectors.toList());
        Map<ItemType, List<ScenarioItemCO>> map = new LinkedHashMap<>();
        for (ItemType itemType : typeEnums) {
            final List<ScenarioItem> itemList = Arrays.stream(scenarioItems)
                    .filter(item -> item.getItemType() == itemType)
                    .collect(Collectors.toList());
            map.put(itemType, getScenarioItemCoList(itemType, itemList, configMap));
        }
        return map;
    }

    public static List<ScenarioItemCO> getScenarioItemCoList(ItemType typeEnum,
                                                             ScenarioItem[] itemArray,
                                                             Map<String, Map<String, Object>> configMap) {
        return getScenarioItemCoList(typeEnum, Arrays.asList(itemArray), configMap);
    }

    public static List<ScenarioItemCO> getScenarioItemCoList(ItemType typeEnum,
                                                             List<ScenarioItem> itemList,
                                                             Map<String, Map<String, Object>> configMap) {
        List<ScenarioItemCO> list = new ArrayList<>();
        final Map<String, Object> itemKeyValueMap = getItemKeyValueMap(typeEnum, configMap);
        if (CollectionUtil.isEmpty(itemKeyValueMap)) {
            return list;
        }

        for (ScenarioItem scenarioItem : itemList) {
            final Object itemValue = scenarioItem.getValue(itemKeyValueMap);
            ScenarioItemCO itemCo = new ScenarioItemCO();
            BeanUtil.copyProperties(scenarioItem, itemCo);
            itemCo.setItemType(scenarioItem.getItemType().getName());
            itemCo.setItemKey(scenarioItem.getItemKey());
            itemCo.setItemValue(String.valueOf(itemValue));
            itemCo.setItemValueType(scenarioItem.getItemValueType().getName());
            itemCo.setDescription(scenarioItem.getDescription());
            list.add(itemCo);
        }
        return list;
    }


    public static ScenarioItemCO toItemCO(ScenarioItemEntity itemEntity) {
        ScenarioItemCO itemCo = new ScenarioItemCO();
        BeanUtil.copyProperties(itemEntity, itemCo);
        return itemCo;
    }

    public static ScenarioItemEntity toItemEntity(ScenarioItemCO itemCo) {
        ScenarioItemEntity itemEntity = new ScenarioItemEntity();
        BeanUtil.copyProperties(itemCo, itemEntity);
        return itemEntity;
    }
}
