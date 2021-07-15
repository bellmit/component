package com.lyloou.component.tool.captcha;


import cn.hutool.core.util.IdUtil;
import com.lyloou.component.dto.SingleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 处理登录
 */
@RestController
@RequestMapping("captcha")
@Api(tags = "【component】工具-图形验证码")
@Slf4j
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;


    @ApiOperation("获取图形验证码")
    @GetMapping(value = "/getCaptcha")
    public SingleResponse<CaptchaVO> getCaptcha() {
        String uniqueId = IdUtil.fastSimpleUUID();
        final String captchaKey = "captcha-key::" + uniqueId;
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaKey(captchaKey);
        captchaVO.setImageBase64Data(captchaService.getCaptcha(captchaKey));

        // DEBUG模式下，直接把code返回出去，方便调试
        if (log.isDebugEnabled()) {
            captchaVO.setCaptchaCode(captchaService.getCaptchaCode(captchaKey));
            log.debug("from captcha: captchaKey={}&captchaCode={}", captchaVO.getCaptchaKey(), captchaVO.getCaptchaCode());
        }
        return SingleResponse.buildSuccess(captchaVO);
    }

}
