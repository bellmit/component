package com.lyloou.component.security.loginvalidator.properties;

import com.lyloou.component.security.loginvalidator.cache.DataCacheProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author huan.fu 2020-06-07 - 14:22
 */
@Data
@ConfigurationProperties(prefix = "component.token")
public class TokenProperties {
    /**
     * 加密所需密钥
     */
    private String secretKey = "eRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93a";

    /**
     * 认证头的header名字
     */
    private String authorizationHeaderName = "Authorization";

    /**
     * http header 中请求头的前缀
     */
    private String authorizationHeaderPrefix = "Bearer ";

    /**
     * jwt 中 token 过期的秒数，默认两天:60*60*2
     */
    private Long expireSecond = 60 * 60 * 24 * 7L;

    /**
     * jwt 中 refresh token的过期时间，7天: 60*60*24*7
     */
    private Long refreshTokenExpiredSecond = 60 * 60 * 24 * 30L;
    /**
     * refresh header 的名字
     */
    private String refreshTokenHeaderName = "RefreshToken";


    private String userIdFieldName = "userId";

    @NestedConfigurationProperty
    private DataCacheProperties cache = new DataCacheProperties();
}
