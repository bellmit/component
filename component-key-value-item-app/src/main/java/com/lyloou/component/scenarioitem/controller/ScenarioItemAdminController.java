package com.lyloou.component.scenarioitem.controller;


import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import com.lyloou.component.scenarioitem.dto.command.ScenarioItemDeleteCmd;
import com.lyloou.component.scenarioitem.dto.command.ScenarioItemSaveOrUpdateCmd;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemGetQry;
import com.lyloou.component.scenarioitem.dto.command.query.ScenarioItemListQry;
import com.lyloou.component.scenarioitem.service.ScenarioItemAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 通用 前端控制器
 * </p>
 *
 * @author lilou
 * @since 2021-03-17
 */
@RestController
@Api(tags = "[通用]-场景项API")
@RequestMapping("/admin/scenario-item")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class ScenarioItemAdminController {
    private final ScenarioItemAdminService scenarioItemAdminService;

    @ApiOperation(value = "获取场景项列表", notes = "根据参数获取场景项列表")
    @GetMapping("list")
    public SingleResponse<List<ScenarioItemCO>> listScenarioItem(@Valid ScenarioItemListQry qry) {
        return SingleResponse.buildSuccess(scenarioItemAdminService.listScenarioItem(qry));
    }

    @ApiOperation(value = "获取场景项", notes = "根据参数获取具体场景项")
    @GetMapping("get")
    public SingleResponse<ScenarioItemCO> getScenarioItem(@Valid ScenarioItemGetQry qry) {
        return SingleResponse.buildSuccess(scenarioItemAdminService.getScenarioItem(qry));
    }

    @ApiOperation(value = "删除场景项", notes = "根据参数删除指定的场景项")
    @PostMapping("delete")
    public SingleResponse<Boolean> deleteScenarioItem(@Valid ScenarioItemDeleteCmd cmd) {
        return SingleResponse.buildSuccess(scenarioItemAdminService.deleteScenarioItem(cmd));
    }

    @ApiOperation(value = "保存或更新场景项", notes = "根据传入的参数信息保存或更新参数")
    @PostMapping("saveOrUpdate")
    public SingleResponse<Boolean> saveOrUpdateScenarioItem(@Valid @RequestBody ScenarioItemSaveOrUpdateCmd cmd) {
        return SingleResponse.buildSuccess(scenarioItemAdminService.saveOrUpdateScenarioItem(cmd));
    }
}
