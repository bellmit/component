package com.lyloou.component.keyvalueitem.dto.command.query;

import com.lyloou.component.keyvalueitem.dto.command.CommonCommand;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author lilou
 * @since 2021/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "列出片单列表参数实体")
public class KeyValueItemPageQry extends CommonCommand {

    @ApiModelProperty(value = "项名称", required = false)
    private String itemName;

    @ApiModelProperty(value = "项键", required = false)
    private String itemKey;

    @NotNull(message = "页码不能为空") @Min(value = 1, message = "页码要大于0")
    @ApiModelProperty(value = "页数", required = true)
    Integer pageNo;

    @ApiModelProperty(value = "页大小", required = true)
    @NotNull(message = "每页大小不能为空") @Min(value = 1, message = "每页大小要大于0")
    Integer pageSize;
}
