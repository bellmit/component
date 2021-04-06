package com.lyloou.component.redismanager;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author lilou
 */
public interface RedisService {
    /**
     * 存储String 类型的键值对
     *
     * @param key
     * @param value
     */
    void set(final String key, final String value);

    <T extends Serializable> void set(final String key, final T entity) throws IOException;

    /**
     * 获取String 类型的键值对
     *
     * @param key
     * @return
     */
    String get(final String key);

    /**
     * @param key
     * @param <T>
     * @return
     */
    <T extends Serializable> T getEntity(final String key);


    /**
     * @param key
     * @return
     */
    Boolean exists(final String key);

    void del(String key);


    void set(final byte[] key, final byte[] value);

    byte[] get(final byte[] key);

    Boolean exists(final byte[] key);

    void del(final byte[]... keys);

    /**
     * 存储String 类型的键值对,并设置存活时间
     *
     * @param key
     * @param value
     * @param ttl
     */
    void set(final String key, final String value, final int ttl);

    /**
     * 存储键类型为String,值类型为对象(pojo,HashMap,ArrayList)并设置存活时间
     *
     * @param key
     * @param entity
     * @param ttl
     * @param <T>
     */
    <T extends Serializable> void set(final String key, final T entity, final int ttl);

    /**
     * key 剩余时间
     *
     * @param key
     * @return
     */
    Long ttl(final String key);

    /**
     * key 已经保存了多长时间
     *
     * @param key
     * @return
     */
    int passed(final String key);

    /**
     * 批量设置redisKEY的存活时间
     *
     * @param seconds
     * @param keys
     */
    void expire(final int seconds, final String... keys);

    /**
     * @param keyPattern
     */
    void delPattern(String keyPattern);

    /**
     * 自增
     *
     * @param key redis key
     */
    Long incr(String key);

    Long decr(String key);

    Double zincrby(String key, Double score, String value);

    Boolean zadd(String key, Long score, String value);

    Double zscore(String key, String value);

    Map<String, Double> valueToScore(String key, final int offset, int count);

    Long sadd(String key, String value);

    Long srem(String key, String value);

    Long scard(String key);

    Boolean sismember(String key, String value);

    Cursor<RedisZSetCommands.Tuple> zscan(String key, String match);

    boolean lock(String key, int timeout);


    void unlock(String key);

    void doWithLock(String key, int timeout, Consumer<Boolean> consumer);

    Set<String> keys(String prefix);
}
