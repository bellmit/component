package com.lyloou.component.scenarioitem.scenario.activity.executor;

import com.lyloou.component.scenarioitem.convertor.ScenarioItemConvertor;
import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;
import com.lyloou.component.scenarioitem.repository.service.ScenarioItemService;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ActivityConst;
import com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject.ActivityCO;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.query.ActivityGetQry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityGetQryExe {
    private final ScenarioItemService scenarioItemService;

    public ActivityCO execute(ActivityGetQry qry) {
        final ActivityCO activity = new ActivityCO();
        final Integer activityId = qry.getId();
        activity.setId(activityId);

        final List<ScenarioItemEntity> itemEntityList = scenarioItemService.lambdaQuery()
                .eq(ScenarioItemEntity::getScenarioId, activityId)
                .eq(ScenarioItemEntity::getScenarioType, ActivityConst.SCENARIO_TYPE)
                .list();
        final Map<String, Map<String, Object>> configMap = ScenarioItemConvertor.toConfigMap(itemEntityList);
        activity.setConfigMap(configMap);
        return activity;
    }

}
