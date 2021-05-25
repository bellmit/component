package com.lyloou.component.scenarioitem.scenario.activity.dto.command;

import com.lyloou.component.dto.Command;
import com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject.ActivityCO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Data
public class ActivitySaveOrUpdateCmd extends Command {
    @ApiModelProperty(value = "活动详情", required = true)
    @NotNull(message = "活动详情不能为空")
    @Valid
    ActivityCO activity;
}
