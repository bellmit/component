package com.lyloou.component.scenarioitem.executor;

import com.lyloou.component.scenarioitem.convertor.ScenarioItemConvertor;
import com.lyloou.component.scenarioitem.dto.command.ScenarioItemSaveOrUpdateCmd;
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
public class ScenarioItemSaveOrUpdateCmdExe {
    private final ScenarioItemService scenarioItemService;

    public Boolean execute(ScenarioItemSaveOrUpdateCmd cmd) {
        final ScenarioItemEntity itemEntity = ScenarioItemConvertor.toItemEntity(cmd.getScenarioItem());
        return scenarioItemService.saveOrUpdate(itemEntity);
    }
}
