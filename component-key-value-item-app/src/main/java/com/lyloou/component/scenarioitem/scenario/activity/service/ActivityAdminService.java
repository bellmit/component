package com.lyloou.component.scenarioitem.scenario.activity.service;

import com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject.ActivityCO;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.ActivityDeleteCmd;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.ActivitySaveOrUpdateCmd;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.query.ActivityGetQry;
import com.lyloou.component.scenarioitem.scenario.activity.executor.ActivityDeleteCmdExe;
import com.lyloou.component.scenarioitem.scenario.activity.executor.ActivityGetQryExe;
import com.lyloou.component.scenarioitem.scenario.activity.executor.ActivitySaveOrUpdateCmdExe;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityAdminService {
    private final ActivitySaveOrUpdateCmdExe activitySaveOrUpdateCmdExe;
    private final ActivityGetQryExe activityGetQryExe;
    private final ActivityDeleteCmdExe activityDeleteCmdExe;

    // 保存或更新活动
    public Boolean saveOrUpdateActivity(ActivitySaveOrUpdateCmd cmd) {
        return activitySaveOrUpdateCmdExe.execute(cmd);
    }

    public ActivityCO getActivity(ActivityGetQry qry) {
        return activityGetQryExe.execute(qry);
    }

    public Boolean deleteActivity(ActivityDeleteCmd cmd) {
        return activityDeleteCmdExe.execute(cmd);
    }
}
