package com.lyloou.component.keyvalueitem.dto.command;


import com.lyloou.component.keyvalueitem.dto.DTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 整个应用通用的Command
 *
 * @author lilou
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCommand extends DTO {
    @ApiModelProperty(value = "操作人", hidden = true)
    private String operator;
}
