package com.lyloou.component.security.loginvalidator;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public class UserManager {

    public static final String X_USER_ID = "x-user-id";
    public static final String X_USER_NAME = "x-user-name";
    public static final String X_USER_IP = "x-user-ip";

    /**
     * 如果有其他信息，可以拼接到这里（json对象字符串）
     */
    public static final String X_USER_INFO = "x-user-info";

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public static Integer getUserId() {
        String userId = getItem(X_USER_ID);
        if (StrUtil.isNotBlank(userId) && NumberUtil.isNumber(userId)) {
            return Integer.parseInt(userId);
        }
        return -1;
    }

    private static String getItem(String itemName) {
        UserContextHolder instance = UserContextHolder.getInstance();
        if (null == instance) {
            return null;
        }
        Map<String, String> context = instance.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return null;
        }
        return context.get(itemName);
    }

    /**
     * 获取用户名称
     *
     * @return
     */
    public static String getUserName() {
        String userName = getItem(X_USER_NAME);
        if (StrUtil.isNotBlank(userName)) {
            return userName;
        }
        return StrUtil.EMPTY;
    }

    public static String getUserIP() {
        return getItem(X_USER_IP);
    }

    public static String getUserInfo() {
        return getItem(X_USER_INFO);
    }
}
