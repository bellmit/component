package com.lyloou.component.security.loginvalidator.controller;

import com.lyloou.component.security.loginvalidator.UserManager;
import com.lyloou.component.security.loginvalidator.annotation.ValidateLogin;

/**
 * 继承此 controller，就可以实现Token身份认证
 *
 * @author lilou
 */
@ValidateLogin
public abstract class BaseTokenController {

    /**
     * 在子类中可以直接获取用户id
     *
     * @return
     */
    protected Integer currentUserId() {
        return UserManager.getUserId();
    }
}