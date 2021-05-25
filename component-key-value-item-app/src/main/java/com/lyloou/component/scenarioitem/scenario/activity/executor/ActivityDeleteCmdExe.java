package com.lyloou.component.scenarioitem.scenario.activity.executor;

import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;
import com.lyloou.component.scenarioitem.repository.service.ScenarioItemService;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ActivityConst;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.ActivityDeleteCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityDeleteCmdExe {
    private final ScenarioItemService scenarioItemService;

    public Boolean execute(ActivityDeleteCmd cmd) {
        return scenarioItemService.lambdaUpdate()
                .eq(ScenarioItemEntity::getScenarioId, cmd.getId())
                .eq(ScenarioItemEntity::getScenarioType, ActivityConst.SCENARIO_TYPE)
                .remove();
    }

}
