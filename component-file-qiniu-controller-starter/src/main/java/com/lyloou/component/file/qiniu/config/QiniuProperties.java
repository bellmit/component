package com.lyloou.component.file.qiniu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lilou
 * @since 2021/4/29
 */
@Data
@ConfigurationProperties(prefix = "file.qiniu")
public class QiniuProperties {
    String accessKey;
    String secretKey;
    String bucket;
    String bucketUrl;
    String uploadDir;
}
