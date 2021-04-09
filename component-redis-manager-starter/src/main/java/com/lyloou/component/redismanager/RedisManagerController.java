package com.lyloou.component.redismanager;

import com.lyloou.component.dto.Result;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>监控管理api</p>
 *
 * @author lilou
 * @since 2021/3/7
 */
@RestController
@RequestMapping("/redismanager")
public class RedisManagerController {

    @Autowired
    RedisManagerService redisManagerService;

    /**
     * 获取本项目中所有可控的key前缀前缀
     *
     * @return 结果
     */
    @RequestMapping("/list")
    public Result list() {
        return Result.success(redisManagerService.listPrefix());
    }

    /**
     * 根据前缀获取所有的 key
     *
     * @param prefix 前缀
     * @return 结果
     */
    @RequestMapping("/keys")
    public Result keys(String prefix) {
        return Result.success(redisManagerService.keys(prefix));
    }

    /**
     * 删除缓存
     *
     * @param prefix 前缀
     * @param key    键
     * @return 结果
     */
    @RequestMapping("/del")
    public Result del(@RequestParam(required = true) String prefix,
                      @RequestParam(required = false) String key) {
        final boolean result = redisManagerService.del(prefix, key);
        return Result.success(String.format("删除缓存：%s::%s，结果:%s", prefix, key, result));
    }

    /**
     * 设置key过期时间
     * 也可以通过 spring.redis.cache-config-list 来单独配置
     *
     * @param prefix 前缀
     * @param key    键
     * @param ttl    超时
     * @return 结果
     */
    @RequestMapping("/expire")
    public Result expire(@RequestParam(required = true) String prefix,
                         @RequestParam(required = false) String key,
                         @RequestParam(required = false) Integer ttl
    ) {
        final boolean result = redisManagerService.expire(prefix, key, ttl);
        return Result.success(String.format("设置缓存过期：%s::%s，ttl:%s, 结果:%s", prefix, key, ttl, result));
    }

    /**
     * 根据具体的key删除缓存
     *
     * @param key 键
     * @return 结果
     */
    @RequestMapping("/delKey")
    public Result del(String key) {
        if (Strings.isEmpty(key)) {
            return Result.error("key is invalid");
        }

        final boolean result = redisManagerService.del(key);
        return Result.success(String.format("删除缓存：%s，结果:%s", key, result));
    }
}
