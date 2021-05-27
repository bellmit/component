package com.lyloou.component.scenarioitem.dto.command;

import com.lyloou.component.dto.Command;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Data
public class ScenarioItemDeleteCmd extends Command {
    @ApiModelProperty(value = "场景类型（如：活动）", required = true)
    private String scenarioType;

    @ApiModelProperty(value = "场景id（如活动ID为：1）", required = true)
    private String scenarioId;

    @ApiModelProperty(value = "项类型（如：抽奖活动，签到活动）", required = false)
    private String itemType;

    @ApiModelProperty(value = "项key（如：默认抽奖次数、文字规则、转盘图片）", required = false)
    private String itemKey;
}