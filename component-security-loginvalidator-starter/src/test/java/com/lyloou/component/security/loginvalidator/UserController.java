package com.lyloou.component.security.loginvalidator;

import cn.hutool.json.JSONUtil;
import com.lyloou.component.security.loginvalidator.annotation.IgnoreValidateLogin;
import com.lyloou.component.security.loginvalidator.annotation.ValidateLogin;
import com.lyloou.component.security.loginvalidator.controller.BaseTokenController;
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
public class UserController extends BaseTokenController {

    @Autowired
    TokenService tokenService;

    // 从父类继承了ValidateLogin，需要身份验证
    @GetMapping("/ping")
    public String ping() {
        final Integer userId = currentUserId();
        System.out.println(userId);
        return "pong";
    }

    // 手动忽略身份验证
    @IgnoreValidateLogin
    @GetMapping("/login")
    public String login(String userId, String username) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", username);
        map.put("userAvatar", "http://cdn.lyloou.com/a.jpg");

        final String token = tokenService.createToken(userId, username, JSONUtil.toJsonStr(map));
        return token;
    }

    // 手动添加身份验证
    @ValidateLogin
    @GetMapping("userinfo")
    public Map<String, String> userInfo() {
        Map<String, String> map = new HashMap<>();
        map.put(UserManager.X_USER_ID, UserManager.getUserId() + "");
        map.put(UserManager.X_USER_IP, UserManager.getUserIP());
        map.put(UserManager.X_USER_NAME, UserManager.getUserName());
        map.put(UserManager.X_USER_INFO, UserManager.getUserInfo());
        return map;
    }
}
