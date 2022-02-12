package com.lyloou.component.redismanager;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author lilou
 * @since 2021/4/6
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisManagerService redisManagerService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("getUser")
    public void getUser() {
        log.info("user 1s:{}", userService.getUser(1));
        log.info("user 2s:{}", userService.getUser(1));
        log.info("user 3s:{}", userService.getUser(1));

        log.info("user 2:{}", userService.getUser(2));
    }

    @RequestMapping("saveOrUpdateUser")
    public void saveOrUpdateUser() {
        final int id = 2;
        log.info("user 2:{}", userService.getUser(id));
        final User user = userService.getUser(id);
        user.setName("user2");
        user.setAge(34);
        userService.saveOrUpdateUser(user);
        // log.info("user 32:{}", userService.cachePerson(id));
        log.info("user 2:{}", userService.getUser(id));

    }

    @RequestMapping("keysTest")
    public void keysTest() {
        User user = userService.getUser(1);
        log.info("user 1:{}", user);
        log.info("user 2:{}", userService.getUser(2));
        log.info("user 3:{}", userService.getUser(3));
        log.info("user 4:{}", userService.getUser(4));
        log.info("user 5:{}", userService.getUser(5));
        log.info("user 6:{}", userService.getUser(6));
        log.info("user 7:{}", userService.getUser(7));
        log.info("person 2:{}", userService.getPerson(3));

        System.out.println();
        log.info("keys:{}", redisTemplate.keys("com.lyloou.component.redismanager.user::*"));

        System.out.println();
        log.info("keys:{}", redisTemplate.keys("com.lyloou.component.redismanager.user"));

        System.out.println();
        log.info("keys:{}", redisTemplate.keys("com.lyloou.component.redismanager"));

        System.out.println();
        log.info("keys:{}", redisTemplate.keys("com.lyloou.component.redismanager.*"));

        redisService.set("user-1", JSONUtil.toJsonStr(user));
        redisService.set("user-2", user);
        redisService.set("user-3", null);
        redisService.set("com.lyloou.component.redismanager.user::9", "adsfdff");
    }

    @RequestMapping("ttlTest")
    public void ttlTest() {
        redisManagerService.expire(CacheNames.TEST_USER, "1", 0);
        redisManagerService.expire(CacheNames.TEST_USER, "2", 0);
        redisManagerService.expire(CacheNames.TEST_USER, "3", 0);
        redisManagerService.expire(CacheNames.TEST_USER, "4", 0);
        redisManagerService.expire(CacheNames.TEST_USER, "5", 0);
        redisManagerService.expire(CacheNames.TEST_USER, "6", 0);
        redisManagerService.expire(CacheNames.TEST_USER, "7", 0);
    }

    @RequestMapping("keyDeleteTest")
    public void keyDeleteTest(Set<String> keys) {
        keysTest();
        Long deletedNum = redisTemplate.delete(keys);
        log.info("deleted num:{}", deletedNum);

        keys = redisTemplate.keys("user-name::*");
        log.info("keys:{}", keys);
        deletedNum = redisTemplate.delete(keys);
        log.info("deleted num:{}", deletedNum);
    }
}