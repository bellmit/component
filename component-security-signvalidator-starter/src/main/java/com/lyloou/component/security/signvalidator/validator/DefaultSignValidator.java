package com.lyloou.component.security.signvalidator.validator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.lyloou.component.cache.datacache.DataCache;
import com.lyloou.component.security.signvalidator.constant.SignConstant;
import com.lyloou.component.security.signvalidator.exception.SignAssert;
import com.lyloou.component.security.signvalidator.exception.SignException;
import com.lyloou.component.security.signvalidator.properties.ClientConfig;
import com.lyloou.component.security.signvalidator.properties.SignProperties;
import com.lyloou.component.security.signvalidator.properties.ValidatorConfig;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lilou
 * @since 2021/7/20
 */

@Component(SignConstant.DEFAULT_SIGN_VALIDATOR)
public class DefaultSignValidator implements SignValidator {
    private final SignProperties signProperties;

    public DefaultSignValidator(SignProperties signProperties) {
        this.signProperties = signProperties;
    }


    @Override
    public boolean isEnabled(String validatorName) {
        final ValidatorConfig validatorConfig = getValidatorConfig(validatorName);
        return this.signProperties.getEnabled() && validatorConfig.getEnabled();
    }

    protected ValidatorConfig getValidatorConfig(String validatorName) {
        final ValidatorConfig validatorConfig = signProperties.getValidators().get(validatorName);
        SignAssert.notNull(validatorConfig, "无效的签名验证器：" + validatorName);
        return validatorConfig;
    }

    @Override
    public boolean checkTimestamp(String validatorName, Long timestamp) {
        final ValidatorConfig validatorConfig = getValidatorConfig(validatorName);
        final long timeout = validatorConfig.getTimeout() * 1000;
        final long now = System.currentTimeMillis();
        if (now - timestamp > timeout) {
            throw new SignException("请求发起时间超过服务器限制时间");
        }
        return true;
    }

    @Override
    public boolean checkClientId(String validatorName, String clientId) {
        if (StrUtil.isEmpty(clientId)) {
            return false;
        }
        final List<ClientConfig> clients = signProperties.getClients();
        // 如果配置中存在，即有效
        return clients.stream().anyMatch(it -> Objects.equals(it.getClientId(), clientId));
    }

    @Override
    public boolean checkSign(String validatorName, Map<String, String> signParameterMap) {
        final String clientId = signParameterMap.get(SignConstant.PARAM_CLIENT_ID);
        final String sign = signParameterMap.get(SignConstant.PARAM_SIGN);
        final List<ClientConfig> clients = signProperties.getClients();
        final ClientConfig clientProperty = clients.stream()
                .filter(it -> Objects.equals(it.getClientId(), clientId))
                .findFirst()
                .orElseThrow(() -> new SignException("没有找到对应的客户端密钥"));

        final List<String> validatorNames = clientProperty.getValidatorNames();
        if (!validatorNames.contains(validatorName)) {
            throw new SignException("此服务不支持此客户端");
        }

        final String clientSecret = clientProperty.getClientSecret();
        String finishSign = buildSign(signParameterMap, clientSecret);
        return Objects.equals(sign, finishSign);
    }


    @Override
    public boolean checkRepeatable(String validatorName, Map<String, String> signParameterMap) {
        DataCache dataCache = SpringUtil.getBean(DataCache.class);
        SignAssert.notNull(dataCache, "无效的缓存配置");

        final String prefix = "COMPONENT::SIGNED::";
        final String clientId = signParameterMap.get(SignConstant.PARAM_CLIENT_ID);
        final String nonce = signParameterMap.get(SignConstant.PARAM_NONCE);
        // key 由【前缀+clientId+随机数】组成
        String key = prefix + clientId + "::" + nonce;
        if (dataCache.containsKey(key)) {
            throw new SignException("请不要发送重复的请求");
        } else {
            dataCache.set(key, "1");
        }
        return true;
    }
}
