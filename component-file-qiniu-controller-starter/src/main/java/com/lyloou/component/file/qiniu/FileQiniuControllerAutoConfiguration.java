package com.lyloou.component.file.qiniu;

import com.lyloou.component.file.qiniu.config.QiniuProperties;
import com.lyloou.component.file.qiniu.controller.QiniuController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author lilou
 * @since 2021/4/29
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({QiniuProperties.class})
public class FileQiniuControllerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(QiniuController.class)
    public QiniuController qiniuController() {
        return new QiniuController();
    }

}
