package com.lyloou.component.security.signvalidator.cache;

import com.lyloou.component.security.signvalidator.properties.SignProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 默认的缓存实现
 *
 * @author lilou
 * @since 2021/7/6
 */
public class DataDefaultCache implements DataCache {

    private static final Map<String, CacheData> CODE_CACHE = new HashMap<>();
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = cacheLock.writeLock();
    private final Lock readLock = cacheLock.readLock();
    private final SignProperties signProperties;

    public DataDefaultCache(SignProperties signProperties) {
        this.signProperties = signProperties;
        final DataCacheProperties cache = signProperties.getCache();
        if (cache.isSchedulePrune()) {
            this.schedulePrune(cache.getTimeout());
        }
    }

    public void schedulePrune(long delay) {
        DataCacheScheduler.INSTANCE.schedule(this::pruneCache, delay);
    }

    /**
     * 设置缓存
     *
     * @param key   缓存KEY
     * @param value 缓存内容
     */
    @Override
    public void set(String key, String value) {
        set(key, value, signProperties.getCache().getTimeout());
    }

    @Override
    public void set(String key, String value, long timeout) {
        writeLock.lock();
        try {
            CODE_CACHE.put(key, new CacheData(value, timeout));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String get(String key) {
        readLock.lock();
        try {
            CacheData code = CODE_CACHE.get(key);
            if (null == code || code.isExpired()) {

                return null;
            }
            return code.getData();
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
            final CacheData code = CODE_CACHE.get(key);
            return null != code && !code.isExpired();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void pruneCache() {
        final Iterator<CacheData> values = CODE_CACHE.values().iterator();
        CacheData cacheCode;
        while (values.hasNext()) {
            cacheCode = values.next();
            if (cacheCode.isExpired()) {
                values.remove();
            }
        }
    }

    @Getter
    @Setter
    private static class CacheData {
        /**
         * 数据
         */
        private final String data;
        /**
         * 过期时间，单位：秒
         */
        private final long expire;

        public CacheData(String data, long expire) {
            this.data = data;
            // 实际过期时间等于当前时间加上有效期
            this.expire = System.currentTimeMillis() + expire * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > this.expire;
        }
    }
}
