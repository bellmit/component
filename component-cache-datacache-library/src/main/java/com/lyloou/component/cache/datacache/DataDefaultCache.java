package com.lyloou.component.cache.datacache;

import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 默认的缓存实现，可配置过期自动清理功能
 *
 * @author lilou
 * @since 2021/7/6
 */
public class DataDefaultCache implements DataCache {

    private static final Map<String, CacheData> CACHE_DATA = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = cacheLock.writeLock();
    private final Lock readLock = cacheLock.readLock();
    private final DataCacheProperties dataCacheProperties;

    public DataDefaultCache(DataCacheProperties dataCacheProperties) {
        this.dataCacheProperties = dataCacheProperties;
        final DataCacheProperties cache = this.dataCacheProperties;
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
        set(key, value, dataCacheProperties.getTimeout());
    }

    @Override
    public void set(String key, String value, long timeout) {
        writeLock.lock();
        try {
            CACHE_DATA.put(key, new CacheData(value, timeout));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String get(String key) {
        readLock.lock();
        try {
            CacheData data = CACHE_DATA.get(key);
            if (invalid(data)) {
                return null;
            }
            return data.getData();
        } finally {
            readLock.unlock();
        }
    }

    private boolean invalid(CacheData cacheData) {
        return null == cacheData || (this.dataCacheProperties.isSchedulePrune() && cacheData.isExpired());
    }

    @Override
    public void remove(String key) {
        writeLock.lock();
        try {
            CACHE_DATA.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean containsKey(String key) {
        readLock.lock();
        try {
            final CacheData data = CACHE_DATA.get(key);
            return !invalid(data);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void pruneCache() {
        final Iterator<CacheData> values = CACHE_DATA.values().iterator();
        CacheData data;
        while (values.hasNext()) {
            data = values.next();
            if (data.isExpired()) {
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
