package com.lyloou.tool.captcha.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 默认得缓存实现
 *
 * @author lilou
 * @since 2021/7/6
 */
public class CodeDefaultCache implements CodeCache {
    public static final CodeDefaultCache INSTANCE = new CodeDefaultCache();

    private static final Map<String, CacheCode> CODE_CACHE = new HashMap<>();
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = cacheLock.writeLock();
    private final Lock readLock = cacheLock.readLock();

    public CodeDefaultCache() {
        if (CodeCacheConfig.schedulePrune) {
            this.schedulePrune(CodeCacheConfig.timeout);
        }
    }

    public void schedulePrune(long delay) {
        CodeCacheScheduler.INSTANCE.schedule(this::pruneCache, delay);
    }

    /**
     * 设置缓存
     *
     * @param key   缓存KEY
     * @param value 缓存内容
     */
    @Override
    public void set(String key, String value) {
        set(key, value, CodeCacheConfig.timeout);
    }

    @Override
    public void set(String key, String value, long timeout) {
        writeLock.lock();
        try {
            CODE_CACHE.put(key, new CacheCode(value, timeout));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String get(String key) {
        readLock.lock();
        try {
            CacheCode code = CODE_CACHE.get(key);
            if (null == code || code.isExpired()) {

                return null;
            }
            return code.getCode();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void remove(String key) {
        writeLock.lock();
        try {
            CODE_CACHE.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean containsKey(String key) {
        readLock.lock();
        try {
            final CacheCode code = CODE_CACHE.get(key);
            return null != code && !code.isExpired();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void pruneCache() {
        final Iterator<CacheCode> values = CODE_CACHE.values().iterator();
        CacheCode cacheCode;
        while (values.hasNext()) {
            cacheCode = values.next();
            if (cacheCode.isExpired()) {
                values.remove();
            }
        }
    }

    @Getter
    @Setter
    private static class CacheCode {
        /**
         * 验证码
         */
        private final String code;
        /**
         * 过期时间，单位：秒
         */
        private final long expire;

        public CacheCode(String code, long expire) {
            this.code = code;
            // 实际过期时间等于当前时间加上有效期
            this.expire = System.currentTimeMillis() + expire * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > this.expire;
        }
    }
}
