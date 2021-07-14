package com.lyloou.tool.captcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.lyloou.tool.captcha.cache.CodeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private CodeCache codeCache;

    @Override
    public String getCaptcha(String captchaKey) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(160, 80);
        String captchaCode = lineCaptcha.getCode();
        codeCache.set(captchaKey, captchaCode, Duration.ofMinutes(5).getSeconds());
        return lineCaptcha.getImageBase64Data();
    }

    @Override
    public String getCaptchaCode(String captchaKey) {
        return codeCache.get(captchaKey);
    }

    @Override
    public Boolean verifyCaptcha(String captchaKey, String captchaCode) {
        String rightCode = codeCache.get(captchaKey);
        codeCache.remove(captchaKey);

        if (Objects.isNull(rightCode)) {
            return false;
        }
        return rightCode.equalsIgnoreCase(captchaCode);
    }

}