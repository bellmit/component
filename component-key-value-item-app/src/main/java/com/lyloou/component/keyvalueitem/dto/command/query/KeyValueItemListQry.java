package com.lyloou.component.keyvalueitem.dto.command.query;

import com.lyloou.component.dto.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * @author lilou
 * @since 2021/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "列出片单列表参数实体")
public class KeyValueItemListQry extends Query {

    @ApiModelProperty(value = "项名称", required = true)
    @NotNull(message = "项名称不能为空")
    private String itemName;

    @ApiModelProperty(value = "项键", required = false)
    private String itemKey;

}
