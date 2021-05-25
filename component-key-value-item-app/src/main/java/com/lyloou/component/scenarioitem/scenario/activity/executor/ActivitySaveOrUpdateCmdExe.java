package com.lyloou.component.scenarioitem.scenario.activity.executor;

import com.lyloou.component.scenarioitem.checker.ScenarioItemChecker;
import com.lyloou.component.scenarioitem.convertor.ScenarioItemConvertor;
import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;
import com.lyloou.component.scenarioitem.repository.service.ScenarioItemService;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ActivityConst;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ScanbuyTypeItem;
import com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject.ActivityCO;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.ActivitySaveOrUpdateCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivitySaveOrUpdateCmdExe {
    private final ScenarioItemService scenarioItemService;

    public Boolean execute(ActivitySaveOrUpdateCmd cmd) {
        final ActivityCO activity = cmd.getActivity();

        // 保存活动信息

        // 保存配置
        saveConfig(activity);
        return null;
    }

    private void saveConfig(ActivityCO activity) {
        final Map<String, Map<String, Object>> configMap = activity.getConfigMap();
        // check
        ScenarioItemChecker.checkConfigMap(ScanbuyTypeItem.values(), configMap);

        // convert
        final List<ScenarioItemEntity> entityList = ScenarioItemConvertor.toScenarioItemEntityList(ScanbuyTypeItem.values(), configMap);

        assignIdIfNeed(activity, entityList);

        // 保存配置
        scenarioItemService.saveOrUpdateBatch(entityList);
    }

    /**
     * 如果已经存在，将之前的id分配过去，以便于 执行 saveOrUpdateBatch 操作
     *
     * @param activity   活动详情
     * @param entityList 实体
     */
    private void assignIdIfNeed(ActivityCO activity, List<ScenarioItemEntity> entityList) {
        final Integer id = activity.getId();
        final Map<String, ScenarioItemEntity> itemTypeKeyToDbEntityMap = scenarioItemService.lambdaQuery()
                .eq(ScenarioItemEntity::getScenarioId, id)
                .eq(ScenarioItemEntity::getScenarioType, ActivityConst.SCENARIO_TYPE)
                .list()
                .stream().collect(Collectors.toMap(this::getUniqId, Function.identity()));
        for (ScenarioItemEntity itemEntity : entityList) {
            itemEntity.setScenarioId(String.valueOf(activity.getId()));
            itemEntity.setScenarioType(ActivityConst.SCENARIO_TYPE);

            final String key = getUniqId(itemEntity);
            Optional.ofNullable(itemTypeKeyToDbEntityMap.get(key))
                    .ifPresent(dbEntity -> itemEntity.setId(dbEntity.getId()));
        }
    }

    private String getUniqId(ScenarioItemEntity itemEntity) {
        return itemEntity.getItemType() + "::" + itemEntity.getItemKey();
    }
}
