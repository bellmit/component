package com.lyloou.component.scenarioitem.dto.clientobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lilou
 */
@Data
public class ScenarioItemCO {

    @ApiModelProperty(value = "项类型（如：抽奖活动，签到活动）")
    private String itemType;

    @ApiModelProperty(value = "项key（如：默认抽奖次数、文字规则、转盘图片）")
    private String itemKey;

    @ApiModelProperty(value = "项值（如：1次、图片url）")
    private String itemValue;

    @ApiModelProperty(value = "项值类型（Integer、String）")
    private String itemValueType;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否必须（可以根据这个做验证）")
    private Boolean required;

}
