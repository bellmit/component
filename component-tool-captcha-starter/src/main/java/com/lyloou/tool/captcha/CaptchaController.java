package com.lyloou.tool.captcha;


import cn.hutool.core.util.IdUtil;
import com.lyloou.component.dto.SingleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 处理登录
 */
@RestController
@RequestMapping("captcha")
public class CaptchaController {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    @Autowired
    CaptchaService captchaService;


    // 获取图形验证码
    @GetMapping(value = "/getCaptcha")
    public SingleResponse<CaptchaVO> getGraphValidateCode() {
        String uniqueId = IdUtil.fastSimpleUUID();
        final String captchaKey = "captcha-key::" + uniqueId;
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaKey(captchaKey);
        captchaVO.setImageBase64Data(captchaService.getCaptcha(captchaKey));

        // DEBUG模式下，直接把code返回出去，方便调试
        if (logger.isDebugEnabled()) {
            captchaVO.setCaptchaCode(captchaService.getCaptchaCode(captchaKey));
            logger.debug("from captcha: captchaKey={}&captchaCode={}", captchaVO.getCaptchaKey(), captchaVO.getCaptchaCode());
        }
        return SingleResponse.buildSuccess(captchaVO);
    }

}
