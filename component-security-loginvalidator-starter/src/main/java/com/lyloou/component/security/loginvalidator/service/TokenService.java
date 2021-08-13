package com.lyloou.component.security.loginvalidator.service;

import cn.hutool.core.util.StrUtil;
import com.lyloou.component.security.loginvalidator.UserManager;
import com.lyloou.component.security.loginvalidator.cache.DataCache;
import com.lyloou.component.security.loginvalidator.properties.TokenProperties;
import com.lyloou.component.security.loginvalidator.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author lilou
 * @since 2021/8/12
 */
@Service
public class TokenService {
    private static final String TOKEN_PREFIX = "TOKEN::";

    @Autowired
    private DataCache dataCache;

    @Autowired
    private TokenProperties tokenProperties;

    public String createToken(String userId, String userName, String userInfoStr) {
        final String jwtToken = createJwtToken(userId, userName);
        cacheUserInfo(jwtToken, userInfoStr);
        return jwtToken;
    }

    private String createJwtToken(String userId, String userName) {
        final HashMap<String, Object> claimsMap = new HashMap<>(2);
        claimsMap.put(UserManager.X_USER_ID, userId);
        claimsMap.put(UserManager.X_USER_NAME, userName);
        final String rawToken = JwtUtils.createJwtToken(claimsMap, tokenProperties.getExpireSecond(), tokenProperties.getSecretKey());
        return tokenProperties.getAuthorizationHeaderPrefix() + rawToken;
    }

    private void cacheUserInfo(String token, String userInfoStr) {
        if (StrUtil.isEmpty(userInfoStr)) {
            userInfoStr = "";
        }
        dataCache.set(TOKEN_PREFIX + token, userInfoStr, tokenProperties.getExpireSecond());
    }

    public String getUserInfo(String token) {
        return dataCache.get(TOKEN_PREFIX + token);
    }
}
