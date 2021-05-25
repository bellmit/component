package com.lyloou.component.scenarioitem.dto.command;

import com.lyloou.component.dto.Command;
import com.lyloou.component.scenarioitem.dto.clientobject.ScenarioItemCO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Data
public class ScenarioItemSaveOrUpdateCmd extends Command {
    @ApiModelProperty(value = "场景项详情", required = true)
    @NotNull(message = "场景项详情不能为空")
    @Valid
    ScenarioItemCO scenarioItem;
}
