package com.lyloou.component.redismanager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author lilou
 * @since 2021/4/6
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@CacheConfig(cacheNames = "bbbbbbbb")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;

    @Override
    @Cacheable(cacheNames = "user-name", key = "#id")
    public User getUser(Integer id) {
        final User user = userRepository.getById(id);
        // 服务类调用，缓存不生效，由于aop调用需要代理（其实也是可以接受的）
        // cachePerson(id);

        // 下面的方式是生效的
        // ApplicationContextHelper.getBean(UserService.class).cachePerson(id);

        return user;
    }

    // @Cacheable(cacheNames = "person", key = "#id")
    @Cacheable(key = "#id")
    @Override
    public User getPerson(Integer id) {
        final User user = userRepository.getById(id);
        return user;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "user-name", key = "#user.id"),
            @CacheEvict(value = "person", allEntries = true)
    })
    public Boolean saveOrUpdateUser(User user) {
        if (user.getId() == null) {
            final int id = new Random().nextInt();
            user.setId(id);
            userRepository.save(id, user);
        } else {
            userRepository.save(user.getId(), user);
        }
        return true;
    }
}
