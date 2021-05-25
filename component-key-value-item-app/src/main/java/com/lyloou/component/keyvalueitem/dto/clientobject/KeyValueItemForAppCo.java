package com.lyloou.component.keyvalueitem.dto.clientobject;

import com.lyloou.component.common.dto.ClientObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 键值项
 * </p>
 *
 * @author lilou
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "KeyValueItemForAppCo对象", description = "键值项")
public class KeyValueItemForAppCo extends ClientObject {

    @ApiModelProperty(value = "项名称")
    private String itemName;

    @ApiModelProperty(value = "项key")
    private String itemKey;

    @ApiModelProperty(value = "项值")
    private String itemValue;

    @ApiModelProperty(value = "备注")
    private String memo;
}
