package com.lyloou.component.security.signvalidator.properties;

import com.lyloou.component.security.signvalidator.constant.SignConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 签名的客户端配置类
 * </p>
 */
@Getter
@Setter
public class ClientConfig {
    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端Secret
     */
    private String clientSecret;

    /**
     * 可以使用哪些验证器名称，默认支持“default”
     */
    private List<String> validatorNames = new ArrayList<String>() {{
        add(SignConstant.DEFAULT_VALIDATOR_NAME);
    }};

}
