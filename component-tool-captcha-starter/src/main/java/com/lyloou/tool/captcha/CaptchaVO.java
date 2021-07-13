package com.lyloou.tool.captcha;

import lombok.Data;

/**
 * @author lilou
 * @since 2021/7/13
 */
@Data
public class CaptchaVO {
    private String captchaKey;
    private String captchaCode;
    private String imageBase64Data;
}
