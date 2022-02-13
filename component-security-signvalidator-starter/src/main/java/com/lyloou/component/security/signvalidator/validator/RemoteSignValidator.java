package com.lyloou.component.security.signvalidator.validator;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyloou.component.security.signvalidator.constant.SignConstant;
import com.lyloou.component.security.signvalidator.properties.SignProperties;
import com.lyloou.component.security.signvalidator.properties.ValidatorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lilou
 * @since 2021/7/20
 */

@Component(SignConstant.REMOTE_SIGN_VALIDATOR)
@Slf4j
public class RemoteSignValidator extends DefaultSignValidator {

    public RemoteSignValidator(SignProperties signProperties) {
        super(signProperties);
    }

    @Override
    public boolean checkSign(String validatorName, Map<String, String> signParameterMap) {
        final ValidatorConfig signItemConfig = getValidatorConfig(validatorName);
        final String preSign = getPreSign(signParameterMap);
        final Map<String, Object> extValues = signItemConfig.getExtValues();

        final String remoteUrl = String.valueOf(extValues.get("remoteUrl"));
        final HttpRequest httpRequest = HttpUtil.createPost(remoteUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).body(preSign).setReadTimeout(5 * 1000).setConnectionTimeout(5 * 1000);
        try (HttpResponse response = httpRequest.execute()) {
            if (response.body() == null) {
                return false;
            }
            String body = response.body();
            JSONObject object = JSON.parseObject(body);
            return 200 == object.getInteger("returnCode");
        } catch (Exception e) {
            log.error("post 请求 【" + remoteUrl + "] 异常, 入参 [" + preSign + "]", e);
        }
        return false;
    }

}
