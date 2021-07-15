package com.lyloou.component.tool.captcha;

import com.lyloou.component.dto.DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lilou
 * @since 2021/7/13
 */
@Data
@ApiModel("验证码信息")
public class CaptchaVO extends DTO {

    @ApiModelProperty("验证码的键")
    private String captchaKey;

    @ApiModelProperty("验证码的值，debug模式下会有正确值")
    private String captchaCode;

    @ApiModelProperty("验证码的Base64表示")
    private String imageBase64Data;
}
