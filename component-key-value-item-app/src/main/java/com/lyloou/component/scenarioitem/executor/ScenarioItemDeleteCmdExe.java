package com.lyloou.component.scenarioitem.executor;

import com.lyloou.component.scenarioitem.dto.command.ScenarioItemDeleteCmd;
import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;
import com.lyloou.component.scenarioitem.repository.service.ScenarioItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScenarioItemDeleteCmdExe {
    private final ScenarioItemService scenarioItemService;

    public Boolean execute(ScenarioItemDeleteCmd cmd) {
        return scenarioItemService.lambdaUpdate()
                .eq(ScenarioItemEntity::getScenarioType, cmd.getScenarioType())
                .eq(ScenarioItemEntity::getScenarioId, cmd.getScenarioId())
                .eq(cmd.getItemType() != null, ScenarioItemEntity::getScenarioType, cmd.getItemType())
                .eq(cmd.getItemKey() != null, ScenarioItemEntity::getScenarioId, cmd.getItemKey())
                .remove();
    }
}
