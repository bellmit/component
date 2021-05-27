package com.lyloou.component.redismanager;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private final Map<String, Boolean> cachePrefixMap = new ConcurrentHashMap<>();


    public Map<String, Map<String, String>> list() {
        Map<String, Map<String, String>> map = new HashMap<>();
        this.cachePrefixMap.forEach((prefix, aBoolean) -> {
            map.put(prefix, getOperationMap(prefix));
        });
        return map;
    }

    private Map<String, String> getOperationMap(String prefix) {
        Map<String, String> map = new HashMap<>();
        map.put("获取所有键", "/keys?prefix=".concat(prefix));
        map.put("根据前缀和key删除", "/del?prefix=".concat(prefix).concat("&key="));
        map.put("设置过期", "/expire?prefix=".concat(prefix).concat("&key=").concat("&ttl="));
        map.put("根据具体key删除", "/delKey?key=".concat(prefix));
        return map;
    }

    // 只允许操作自己的这个项目的key（防止乱删除）
    public boolean isNotValid(String prefix) {
        if (Strings.isEmpty(prefix)) {
            return true;
        }
        final boolean noneMatch = cachePrefixMap.keySet().stream()
                .noneMatch(prefix::startsWith);
        return noneMatch;
    }

    public void unregisterCachePrefix(String cachePrefix) {
        cachePrefixMap.remove(cachePrefix);
    }

    public void registerCachePrefix(String cachePrefix) {
        cachePrefixMap.put(cachePrefix, true);
    }


    // 手动调用删除方法
    public boolean delByWrapKey(String wrapKey) {
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

    public boolean delByPrefixAndKey(@NonNull String prefix, @Nullable String key) {

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
