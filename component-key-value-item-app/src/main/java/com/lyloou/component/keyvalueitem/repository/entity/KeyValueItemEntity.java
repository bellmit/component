package com.lyloou.component.keyvalueitem.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("key_value_item")
@ApiModel(value = "KeyValueItemEntity对象", description = "键值项")
public class KeyValueItemEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项名称")
    private String itemName;

    @ApiModelProperty(value = "项key")
    private String itemKey;

    @ApiModelProperty(value = "项值")
    private String itemValue;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "是否删除：0未删除，1已删除")
    private Integer deleted;


}
