package com.lyloou.tool.captcha;


public interface CaptchaService {
    /**
     * 获取图形验证码时的唯一标识
     *
     * @param captchaKey 唯一标识（如：ip、phone、email等）
     * @return 获取验证码
     */
    String getCaptcha(String captchaKey);

    /**
     * 获取真实的验证码
     *
     * @param captchaKey 唯一标识（如：ip、phone、email等）
     * @return 结果
     */
    String getCaptchaCode(String captchaKey);

    /**
     * 验证图形验证码
     *
     * @param captchaKey  唯一标识（如：ip、phone、email等）
     * @param captchaCode 输入的验证码
     * @return 结果
     */
    Boolean verifyCaptcha(String captchaKey, String captchaCode);
}
