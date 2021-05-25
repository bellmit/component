package com.lyloou.component.scenarioitem.scenario.activity.dto.clientobject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author lilou
 * @since 2021/5/13
 */
@Data
public class ActivityCO {
    @ApiModelProperty(value = "活动ID")
    private Integer id;
    @ApiModelProperty(value = "活动名称")
    private String name;
    @ApiModelProperty(value = "活动封面图URL")
    private String coverImageUrl;

    @ApiModelProperty(value = "活动配置信息")
    private Map<String, Map<String, Object>> configMap;

}
