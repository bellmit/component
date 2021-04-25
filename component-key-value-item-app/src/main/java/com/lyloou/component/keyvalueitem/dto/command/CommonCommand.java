package com.lyloou.component.keyvalueitem.dto.command;


import com.lyloou.component.dto.Command;
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
public class CommonCommand extends Command {
    @ApiModelProperty(value = "操作人", hidden = true)
    private String operator;
}
