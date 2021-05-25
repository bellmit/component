package com.lyloou.component.scenarioitem.scenario.activity.controller;


import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.scenarioitem.dto.ItemType;
import com.lyloou.component.scenarioitem.dto.ScenarioItem;
import com.lyloou.component.scenarioitem.scenario.activity.dto.ScanbuyTypeItem;
import com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject.ActivityCO;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.ActivityDeleteCmd;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.ActivitySaveOrUpdateCmd;
import com.lyloou.component.scenarioitem.scenario.activity.dto.command.query.ActivityGetQry;
import com.lyloou.component.scenarioitem.scenario.activity.service.ActivityAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用 前端控制器
 * </p>
 *
 * @author lilou
 * @since 2021-03-17
 */
@RestController
@Api(tags = "[活动]-活动API")
@RequestMapping("/activity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class ActivityAdminController {
    private final ActivityAdminService activityAdminService;

    @ApiOperation(value = "获取configMap文档", notes = "configMap文档")
    @GetMapping("getConfigMapDoc")
    public SingleResponse<Map<ItemType, List<Map<String, Object>>>> getConfigMapDoc() {

        return SingleResponse.buildSuccess(ScenarioItem.getItemTypeToScenarioItemDocJsonMap(ScanbuyTypeItem.values()));
    }

    @ApiOperation(value = "获取configMap示例", notes = "configMap示例")
    @GetMapping("getConfigMapSample")
    public SingleResponse<Map<ItemType, Map<String, Object>>> getConfigMapSample() {
        return SingleResponse.buildSuccess(ScenarioItem.getItemTypeToScenarioItemSampleJsonMap(ScanbuyTypeItem.values()));
    }

    @ApiOperation(value = "保存或更新活动", notes = "根据传入的参数信息保存或更新参数")
    @PostMapping("saveOrUpdate")
    public SingleResponse<Boolean> saveOrUpdateActivity(@Valid @RequestBody ActivitySaveOrUpdateCmd cmd) {
        return SingleResponse.buildSuccess(activityAdminService.saveOrUpdateActivity(cmd));
    }

    @ApiOperation(value = "删除活动", notes = "根据传入的参数信息删除活动")
    @PostMapping("delete")
    public SingleResponse<Boolean> deleteActivity(@Valid @RequestBody ActivityDeleteCmd cmd) {
        return SingleResponse.buildSuccess(activityAdminService.deleteActivity(cmd));
    }

    @ApiOperation(value = "保存或更新活动", notes = "根据传入的参数信息保存或更新参数")
    @GetMapping("get")
    public SingleResponse<ActivityCO> getActivity(@Valid ActivityGetQry qry) {
        return SingleResponse.buildSuccess(activityAdminService.getActivity(qry));
    }
}
