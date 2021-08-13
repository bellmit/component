package com.lyloou.component.security.loginvalidator;

import com.lyloou.component.security.loginvalidator.annotation.ValidateLogin;
import com.lyloou.component.security.loginvalidator.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lilou
 * @since 2021/8/12
 */
@RestController
public class UserController {
    @Autowired
    TokenService tokenService;

    @GetMapping("/login")
    public String login(String userId, String username) {
        final String token = tokenService.createToken(userId, username, username);
        return token;
    }

    @ValidateLogin
    @GetMapping("userinfo")
    public Map<String, String> userInfo(String token) {
        Map<String, String> map = new HashMap<>();
        final String userInfo = tokenService.getUserInfo(token);
        map.put("userInfo", userInfo);
        map.put(UserManager.X_USER_ID, UserManager.getUserId() + "");
        map.put(UserManager.X_USER_IP, UserManager.getUserIP());
        map.put(UserManager.X_USER_NAME, UserManager.getUserName());
        return map;
    }
}
