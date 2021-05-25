package com.lyloou.component.scenarioitem.service.impl;

import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import com.lyloou.component.scenarioitem.dto.command.ScenarioItemDeleteCmd;
import com.lyloou.component.scenarioitem.dto.command.ScenarioItemSaveOrUpdateCmd;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemGetQry;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemListQry;
import com.lyloou.component.scenarioitem.executor.ScenarioItemDeleteCmdExe;
import com.lyloou.component.scenarioitem.executor.ScenarioItemSaveOrUpdateCmdExe;
import com.lyloou.component.scenarioitem.executor.query.ScenarioItemGetQryExe;
import com.lyloou.component.scenarioitem.executor.query.ScenarioItemListQryExe;
import com.lyloou.component.scenarioitem.service.ScenarioItemAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScenarioItemAdminServiceImpl implements ScenarioItemAdminService {
    private final ScenarioItemListQryExe scenarioItemListQryExe;
    private final ScenarioItemGetQryExe scenarioItemGetQryExe;
    private final ScenarioItemDeleteCmdExe scenarioItemDeleteCmdExe;
    private final ScenarioItemSaveOrUpdateCmdExe scenarioItemSaveOrUpdateCmdExe;

    @Override
    public List<ScenarioItemCO> listScenarioItem(ScenarioItemListQry qry) {
        return scenarioItemListQryExe.execute(qry);
    }

    @Override
    public ScenarioItemCO getScenarioItem(ScenarioItemGetQry qry) {
        return scenarioItemGetQryExe.execute(qry);
    }

    @Override
    public Boolean deleteScenarioItem(ScenarioItemDeleteCmd cmd) {
        return scenarioItemDeleteCmdExe.execute(cmd);
    }

    @Override
    public Boolean saveOrUpdateScenarioItem(ScenarioItemSaveOrUpdateCmd cmd) {
        return scenarioItemSaveOrUpdateCmdExe.execute(cmd);
    }
}
