package com.lyloou.component.scenarioitem.scenario.activity;

import com.alibaba.fastjson.JSON;
import com.lyloou.component.scenarioitem.checker.ScenarioItemChecker;
import com.lyloou.component.scenarioitem.convertor.ScenarioItemConvertor;
import com.lyloou.component.scenarioitem.dto.ItemType;
import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ActiveItemType;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ActivityConst;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ScanbuyTypeItem;
import com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject.ActivityCO;
import com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject.ScanbuyCO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/5/13
 */
public class ScanbuyTest {
    public static String requestBody = "{\n" + "  \"id\": 1,\n" + "  \"name\": \"扫码购买\",\n" + "  \"coverImageUrl\": \"http://cdn.lyloou.com/a.jpg\",\n" + "  \"configMap\": {\n" + "    \"common\": {\n" + "      \"ifLogin\": true,\n" + "      \"scanbuyBasicQrCodeProductId\": \"12\",\n" + "      \"scanbuyBasicQrCodeContentShow\": true,\n" + "      \"scanbuyBasicQrCodeContent\": \"我是内容\",\n" + "      \"scanbuyBasicQrCodeContentTextColor\": \"#332\"\n" + "    },\n" + "    \"lottery\": {\n" + "      \"scanbuyLotteryInitialChance\": 2,\n" + "      \"scanbuyLotteryAddChancePerBuy\": 3,\n" + "      \"scanbuyLotteryContentPrimaryTextColor\": \"#777\",\n" + "      \"scanbuyLotteryContentNumberTextColor\": \"#999\",\n" + "      \"scanbuyLotteryTitleTextColor\": \"#fff\"\n" + "    }\n" + "  }\n" + "}\n";

    public static void main(String[] args) throws ClassNotFoundException {
        store();
    }


    private static void store() {
        // get from client
        // 根据 request 体加载
        System.out.println("请求体----------");
        final ActivityCO activityDO = JSON.parseObject(requestBody, ActivityCO.class);
        System.out.println(JSON.toJSONString(activityDO, true));

        System.out.println("验证configMap----------");
        final Map<String, Map<String, Object>> configMap = activityDO.getConfigMap();
        ScenarioItemChecker.checkConfigMap(ScanbuyTypeItem.values(), configMap);


        System.out.println("获取全部组合----------");
        final Map<ItemType, List<ScenarioItemCO>> typeItemListMap = ScenarioItemConvertor.toScenarioItemListMap(ScanbuyTypeItem.values(), configMap);

        System.out.println(JSON.toJSONString(typeItemListMap, true));

        System.out.println("获取单个组合 List<TypeItemCO>----------");
        final List<ScenarioItemCO> typeItemCoList = ScenarioItemConvertor.getScenarioItemCoList(ActiveItemType.common, ScanbuyTypeItem.values(), configMap);
        System.out.println(typeItemCoList);

        System.out.println("获取单个值 scanbuyBasicQrCodeProductId ----------");
        Map<String, Object> itemKeyValueMap = ScenarioItemConvertor.getItemKeyValueMap(ActiveItemType.common, configMap);
        final Integer scanbuyBasicQrCodeProductIdValue = ScanbuyTypeItem.scanbuyBasicQrCodeProductId.getValue(itemKeyValueMap);
        System.out.println(scanbuyBasicQrCodeProductIdValue);

        System.out.println("获取某个ItemCo scanbuyBasicQrCodeContentShow----------");
        final List<ScenarioItemCO> commonTypeItemList = typeItemListMap.get(ActiveItemType.common);
        commonTypeItemList.stream().filter(itemCo -> itemCo.getItemKey().equals(ScanbuyTypeItem.scanbuyBasicQrCodeContentShow.name())).findFirst().ifPresent(System.out::println);


        System.out.println("转换成DO list----------");
        final List<ScenarioItemEntity> itemDOList = commonTypeItemList.stream().map(itemCo -> {
            ScenarioItemEntity itemDO = ScenarioItemConvertor.toItemEntity(itemCo);
            itemDO.setScenarioId(String.valueOf(activityDO.getId()));
            itemDO.setScenarioType(ActivityConst.SCENARIO_TYPE);
            return itemDO;

        }).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(itemDOList, true));

        System.out.println("转换成 map (From DO) ----------");
        itemDOList.get(0).setItemType("anotherType");
        final Map<String, Map<String, Object>> configMap2 = ScenarioItemConvertor.toConfigMap(itemDOList);
        System.out.println(JSON.toJSONString(configMap2, true));

        System.out.println("获取 itemKeyValueMap2 ------------");
        final Map<String, Object> itemKeyValueMap2 = ScenarioItemConvertor.getItemKeyValueMap(ActiveItemType.common, configMap2);
        System.out.println(JSON.toJSONString(itemKeyValueMap2, true));

        System.out.println("获取具体值 ------------");
        final Integer typeItem2 = ScanbuyTypeItem.scanbuyBasicQrCodeProductId.notNullValue(itemKeyValueMap2);
        final Boolean typeItem3 = ScanbuyTypeItem.scanbuyBasicQrCodeContentShow.notNullValue(itemKeyValueMap2);
        System.out.println(typeItem2);
        System.out.println(typeItem3);

        System.out.println("转换成 Instance ------------");
        final ScanbuyCO scanbuyCO = ScenarioItemConvertor.newInstance(ScanbuyTypeItem.values(), configMap, ScanbuyCO.class);
        System.out.println(scanbuyCO);
    }


}
