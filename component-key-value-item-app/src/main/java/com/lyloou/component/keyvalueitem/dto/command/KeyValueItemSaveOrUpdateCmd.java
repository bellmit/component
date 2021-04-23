package com.lyloou.component.keyvalueitem.dto.command;

import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * @author lilou
 * @since 2021/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ApiModel("更新键值项的参数实体")
public class KeyValueItemSaveOrUpdateCmd extends CommonCommand {

    @ApiModelProperty(value = "键值项详情", required = true)
    @NotNull(message = "键值项详情不能为空")
    @Valid
    private KeyValueItemCo keyValueItem;
}
