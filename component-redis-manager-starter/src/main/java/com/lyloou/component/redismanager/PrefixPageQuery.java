package com.lyloou.component.redismanager;

import com.lyloou.component.dto.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lilou
 * @since 2021/8/10
 */
@ApiModel("分页搜索")
@Data
public class PrefixPageQuery extends PageQuery {
    @ApiModelProperty(value = "前缀")
    String prefix;
}
