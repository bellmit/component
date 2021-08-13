package com.lyloou.component.security.loginvalidator.service;

/**
 * @author lilou
 * @since 2021/8/12
 */
public interface TokenService {
    String TOKEN_PREFIX = "USER_TOKEN::";

    String createToken(String userId, String userName, String userInfo);

    String getUserInfo(String token);
}
