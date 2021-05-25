package com.lyloou.component.scenarioitem.executor.query;

import com.lyloou.component.scenarioitem.convertor.ScenarioItemConvertor;
import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemListQry;
import com.lyloou.component.scenarioitem.repository.entity.ScenarioItemEntity;
import com.lyloou.component.scenarioitem.repository.service.ScenarioItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScenarioItemListQryExe {
    private final ScenarioItemService scenarioItemService;

    public List<ScenarioItemCO> execute(ScenarioItemListQry qry) {
        return scenarioItemService
                .lambdaQuery()
                .eq(ScenarioItemEntity::getScenarioType, qry.getScenarioType())
                .eq(ScenarioItemEntity::getScenarioId, qry.getScenarioId())
                .list()
                .stream()
                .map(ScenarioItemConvertor::toItemCO)
                .collect(Collectors.toList());
    }
}
