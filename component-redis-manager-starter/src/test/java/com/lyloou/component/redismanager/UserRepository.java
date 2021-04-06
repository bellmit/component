package com.lyloou.component.redismanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lilou
 * @since 2021/4/6
 */
@Repository
@Slf4j
public class UserRepository {
    private final Map<Integer, User> userMap = new HashMap<Integer, User>() {{
        put(1, new User().setId(1).setName("User1").setAge(18).setSex("男"));
        put(2, new User().setId(2).setName("User2").setAge(28).setSex("女"));
        put(3, new User().setId(3).setName("User3").setAge(38).setSex("女"));
        put(4, new User().setId(4).setName("User4").setAge(48).setSex("男"));
    }};

    public User getById(Integer id) {
        log.info("load data from repository, id:{}", id);
        return userMap.get(id);
    }

    public User save(Integer id, User user) {
        log.info("flush data to repository, id:{}, user:{}", id, user);
        return userMap.put(id, user);
    }
}
