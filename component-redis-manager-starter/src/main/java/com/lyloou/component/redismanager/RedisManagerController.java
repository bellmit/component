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

    @RequestMapping("/listPrefix")
    public Result list() {
        return Result.success(redisManagerService.listPrefix());
    }

    @RequestMapping("/keys")
    public Result keys(String prefix) {
        if (!redisManagerService.isPrefixExisted(prefix)) {
            return Result.error("prefix is invalid");
        }
        return Result.success(redisManagerService.keys(prefix));
    }

    @RequestMapping("/del")
    public Result del(@RequestParam(required = true) String prefix,
                      @RequestParam(required = false) String key) {
        if (!redisManagerService.isPrefixExisted(prefix)) {
            return Result.error("prefix is invalid");
        }

        final boolean result = redisManagerService.del(prefix, key);
        return Result.success(String.format("删除缓存：%s::%s，结果:%s", prefix, key, result));
    }

    @RequestMapping("/delKey")
    public Result del(String key) {
        if (Strings.isEmpty(key)) {
            return Result.error("key is invalid");
        }

        final String[] prefixKeyArray = key.split(RedisManagerService.SEP);
        if (!redisManagerService.isPrefixExisted(prefixKeyArray[0])) {
            return Result.error("key is invalid");
        }

        final boolean result = redisManagerService.del(key);
        return Result.success(String.format("删除缓存：%s，结果:%s", key, result));
    }
}
