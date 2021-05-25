package com.lyloou.component.scenarioitem.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lyloou.component.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通用场景值配置
 * </p>
 *
 * @author lilou
 * @since 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("scenario_item")
@ApiModel(value = "ScenarioItemEntity对象", description = "通用场景值配置")
public class ScenarioItemEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "场景类型（如：活动）")
    private String scenarioType;

    @ApiModelProperty(value = "场景id（如活动ID为：1）")
    private String scenarioId;

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

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "是否删除：0未删除，1已删除")
    private Boolean deleted;


}
