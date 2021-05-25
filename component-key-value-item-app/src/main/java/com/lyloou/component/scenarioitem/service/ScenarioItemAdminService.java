package com.lyloou.component.scenarioitem.service;

import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import com.lyloou.component.scenarioitem.dto.command.ScenarioItemDeleteCmd;
import com.lyloou.component.scenarioitem.dto.command.ScenarioItemSaveOrUpdateCmd;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemGetQry;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemListQry;

import java.util.List;

/**
 * @author lilou
 * @since 2021/5/25
 */
public interface ScenarioItemAdminService {
    List<ScenarioItemCO> listScenarioItem(ScenarioItemListQry qry);

    ScenarioItemCO getScenarioItem(ScenarioItemGetQry qry);

    Boolean deleteScenarioItem(ScenarioItemDeleteCmd cmd);

    Boolean saveOrUpdateScenarioItem(ScenarioItemSaveOrUpdateCmd cmd);
}
