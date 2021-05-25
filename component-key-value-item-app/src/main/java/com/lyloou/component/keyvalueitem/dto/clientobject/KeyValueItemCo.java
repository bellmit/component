package com.lyloou.component.keyvalueitem.dto.clientobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lyloou.component.common.dto.ClientObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

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
@TableName("key_value_item")
@ApiModel(value = "KeyValueItemEntity对象", description = "键值项")
public class KeyValueItemCo extends ClientObject {

    @ApiModelProperty(value = "项ID")
    private Integer id;

    @ApiModelProperty(value = "项名称", required = true)
    @NotNull(message = "项名称不能这空")
    private String itemName;

    @ApiModelProperty(value = "项key", required = true)
    @NotNull(message = "项键不能为空")
    private String itemKey;

    @ApiModelProperty(value = "项值")
    private String itemValue;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "修改人")
    private String modifier;


}
