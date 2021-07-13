package com.lyloou.tool.captcha;

import lombok.Data;

/**
 * @author lilou
 * @since 2021/7/13
 */
@Data
public class LoginQuery extends CaptchaQuery {
    private String username;
    private String password;
}
