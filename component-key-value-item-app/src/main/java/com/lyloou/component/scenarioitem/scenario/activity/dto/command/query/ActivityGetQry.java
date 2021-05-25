package com.lyloou.component.scenarioitem.scenario.activity.dto.command.query;

import com.lyloou.component.dto.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lilou
 * @since 2021/5/25
 */
@Data
public class ActivityGetQry extends Query {
    @ApiModelProperty(value = "活动ID", required = true)
    @NotNull(message = "活动ID不能为空")
    Integer id;
}
