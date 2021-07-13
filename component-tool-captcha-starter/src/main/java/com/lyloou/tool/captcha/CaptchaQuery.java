package com.lyloou.tool.captcha;

import com.lyloou.component.dto.Query;
import lombok.Data;

/**
 * @author lilou
 * @since 2021/7/13
 */
@Data
public class CaptchaQuery extends Query {
    private String captchaKey;
    private String captchaCode;
}
