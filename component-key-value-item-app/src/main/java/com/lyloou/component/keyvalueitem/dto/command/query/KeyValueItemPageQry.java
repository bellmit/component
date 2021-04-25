package com.lyloou.component.keyvalueitem.dto.command.query;

import com.lyloou.component.dto.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author lilou
 * @since 2021/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "列出片单列表参数实体")
public class KeyValueItemPageQry extends PageQuery {

    @ApiModelProperty(value = "项名称", required = false)
    private String itemName;

    @ApiModelProperty(value = "项键", required = false)
    private String itemKey;

}
