package com.lyloou.component.redismanager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("redisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

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
        log.info("user 1:{}", userService.getUser(1));
        log.info("user 2:{}", userService.getUser(2));
        log.info("person 2:{}", userService.getPerson(3));

        Set<String> keys = redisTemplate.keys("user-name::*");
        log.info("keys:{}", keys);
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