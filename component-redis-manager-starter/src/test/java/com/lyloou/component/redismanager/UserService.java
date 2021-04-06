package com.lyloou.component.redismanager;

/**
 * @author lilou
 * @since 2021/4/6
 */
public interface UserService {
    User getUser(Integer id);

    User getPerson(Integer id);

    Boolean saveOrUpdateUser(User user);
}
