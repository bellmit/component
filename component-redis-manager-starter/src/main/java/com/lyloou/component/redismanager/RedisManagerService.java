package com.lyloou.component.redismanager;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控处理类
 *
 * @author lilou
 * @since 2021/3/7
 */
@Service
@Slf4j
public class RedisManagerService {

    @Autowired
    private RedisService redisService;

    public static final String SEP = CacheKeyPrefix.SEPARATOR;
    public static final String STAR = "*";
    public static final String SEP_STAR = SEP + STAR;
    public static final Integer DEFAULT_TTL = 300;

    /**
     * 存放任务的状态的map
     */
    private final Map<String, Boolean> prefixMap = new ConcurrentHashMap<>();


    public Map<String, Boolean> listPrefix() {
        return prefixMap;
    }

    public boolean isNotValid(String prefix) {
        if (Strings.isEmpty(prefix)) {
            return true;
        }
        return false;
    }

    public void putPrefix(String key, Boolean status) {
        prefixMap.put(key, status);
    }


    // 手动调用删除方法
    public boolean del(String wrapKey) {
        try {
            final String[] prefixKeyArray = wrapKey.split(SEP);
            if (isNotValid(prefixKeyArray[0])) {
                return false;
            }
            redisService.del(wrapKey);
            return true;
        } catch (Exception e) {
            log.warn("删除缓存出现异常：wrapKey={}，error={}", wrapKey, e.getMessage());
            return false;
        }
    }

    public boolean del(String prefix, String key) {

        try {
            if (isNotValid(prefix)) {
                return false;
            }

            if (Strings.isEmpty(key)) {
                redisService.delPattern(prefix + SEP_STAR);
                return true;
            }

            final String wrapKey = prefix + SEP + key;
            redisService.del(wrapKey.getBytes());
            return true;
        } catch (Exception e) {
            log.warn("删除缓存出现异常：prefix={}，key={}，error={}", prefix, key, e.getMessage());
            return false;
        }
    }

    public boolean expire(String prefix, String key, Integer ttl) {
        try {
            if (isNotValid(prefix)) {
                return false;
            }

            String wrapKey = prefix;
            if (Strings.isNotEmpty(key)) {
                wrapKey = wrapKey + SEP + key;
            }
            if (ttl == null) {
                ttl = DEFAULT_TTL;
            }
            redisService.expire(ttl, wrapKey);
            return true;
        } catch (Exception e) {
            log.warn("设置ttl失败：prefix={}，key={}，ttl={}，error={}", prefix, key, ttl, e.getMessage());
            return false;
        }
    }

    public Set<String> keys(String prefix) {
        if (isNotValid(prefix)) {
            return new HashSet<>();
        }
        return redisService.keys(prefix + SEP_STAR);
    }
}
