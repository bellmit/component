package com.lyloou.component.scenarioitem.executor.query;

import com.lyloou.component.scenarioitem.convertor.ScenarioItemConvertor;
import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemGetQry;
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
public class ScenarioItemGetQryExe {
    private final ScenarioItemService scenarioItemService;

    public ScenarioItemCO execute(ScenarioItemGetQry qry) {
        return scenarioItemService.lambdaQuery()
                .eq(ScenarioItemEntity::getScenarioType, qry.getScenarioType())
                .eq(ScenarioItemEntity::getScenarioId, qry.getScenarioId())
                .eq(ScenarioItemEntity::getItemType, qry.getItemType())
                .eq(ScenarioItemEntity::getItemKey, qry.getItemKey())
                .oneOpt()
                .map(ScenarioItemConvertor::toItemCO)
                .orElse(null);
    }
}
