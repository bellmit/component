package com.lyloou.component.security.signvalidator.properties;

import com.lyloou.component.security.signvalidator.constant.SignConstant;
import com.lyloou.component.security.signvalidator.validator.DefaultSignValidator;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 签名验证器配置类
 *
 * @author lilou
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatorConfig {


    /**
     * 局部开关
     */
    private Boolean enabled = true;

    /**
     * 验证器的Bean名称
     * 默认为：{@link SignConstant#DEFAULT_SIGN_VALIDATOR}，对应的实现类为 @{@link DefaultSignValidator}
     */
    private String beanName = SignConstant.DEFAULT_SIGN_VALIDATOR;

    /**
     * 超时时长，用来验证时间戳；默认1分钟，单位秒
     */
    private long timeout = 60L;


    /**
     * 扩展参数
     */
    Map<String, Object> extValues = new LinkedHashMap<>();

}
