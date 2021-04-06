package com.lyloou.component.redismanager;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    @Qualifier("redisService")
    private RedisService redisService;
    public static final String SEP = "::";
    public static final String STAR = "*";
    public static final String SEP_STAR = SEP + STAR;

    /**
     * 存放任务的状态的map
     */
    private final Map<String, Boolean> prefixMap = new ConcurrentHashMap<>();


    public Map<String, Boolean> listPrefix() {
        return prefixMap;
    }

    public boolean isPrefixExisted(String prefix) {
        if (Strings.isEmpty(prefix)) {
            return false;
        }
        return prefixMap.containsKey(prefix);
    }

    public void putPrefix(String key, Boolean status) {
        prefixMap.put(key, status);
    }


    // 手动调用删除方法
    public boolean del(String wrapKey) {
        try {

            redisService.del(wrapKey);
            return true;
        } catch (Exception e) {
            log.warn("删除缓存出现异常：wrapKey={}，error={}", wrapKey, e.getMessage());
            return false;
        }
    }

    public boolean del(String prefix, String key) {
        try {
            if (!prefixMap.containsKey(prefix)) {
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

    public Set<String> keys(String prefix) {
        return redisService.keys(prefix + SEP_STAR);
    }
}
