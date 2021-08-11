package com.lyloou.component.redismanager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lilou
 * @since 2021/8/10
 */
@ApiModel("分页搜索结果")
@Data
public class PrefixPageResponse {
    @ApiModelProperty(value = "键")
    String key;

    @ApiModelProperty(value = "值")
    Object value;
}
