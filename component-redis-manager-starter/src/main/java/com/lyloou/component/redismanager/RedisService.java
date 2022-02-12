package com.lyloou.component.redismanager;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author lilou
 */
public interface RedisService {
    /**
     * 获取redisTemplate，以支持更多操作
     *
     * @return template对象
     */
    RedisTemplate<String, Object> getRedisTemplate();

    /**
     * 存储String 类型的键值对
     *
     * @param key   键
     * @param value 值
     */
    void set(final String key, final String value);

    /**
     * 存储String 类型的键值对,并设置存活时间
     *
     * @param key   键
     * @param value 值
     * @param ttl   超时时长，以秒为单位
     */
    void set(final String key, final String value, final int ttl);


    /**
     * 获取String 类型的键值对
     *
     * @param key 键
     * @return 结果
     */
    String get(final String key);

    /**
     * 存储 <code><T></code> 类型的键值对
     *
     * @param key    键
     * @param entity 实体
     * @param <T>    泛型
     */
    <T extends Serializable> void set(final String key, final T entity);


    /**
     * 存储键类型为String,值类型为对象(pojo,HashMap,ArrayList)并设置存活时间
     *
     * @param key    键
     * @param entity 值
     * @param ttl    超时时长
     * @param <T>    泛型
     */
    <T extends Serializable> void set(final String key, final T entity, final int ttl);


    /**
     * 获取实体对象
     *
     * @param key 键
     * @param <T> 泛型
     * @return 结果
     */
    <T extends Serializable> T getEntity(final String key);


    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return 结果
     */
    Boolean exists(final String key);

    /**
     * 删除键
     *
     * @param key 键
     */
    void del(String key);


    /**
     * 设置 byte[] 类型的对象
     *
     * @param key   字节数组类型的键
     * @param value 字节数组类型的值
     */
    void set(final byte[] key, final byte[] value);

    /**
     * 获取实体
     *
     * @param key 字节数组类型的键
     * @return 结果，字节数组类型的值
     */
    byte[] get(final byte[] key);

    /**
     * 批量获取值
     *
     * @param keys
     * @return
     */
    Map<String, Object> multiGet(Collection<String> keys);

    /**
     * 判断键是否存在
     *
     * @param key 字节数组类型的键
     * @return 结果
     */
    Boolean exists(final byte[] key);

    /**
     * 删除键
     *
     * @param keys 字节数组类型键值组
     */
    void del(final byte[]... keys);


    /**
     * key 剩余时间
     *
     * @param key 键
     * @return 结果，时长
     */
    Long ttl(final String key);


    /**
     * 批量设置redisKEY的存活时间
     *
     * @param seconds 多少秒
     * @param keys    键数组
     */
    void expire(final int seconds, final String... keys);

    /**
     * 模糊删除
     *
     * @param keyPattern key模式
     */
    void delPattern(String keyPattern);

    /**
     * 自增
     *
     * @param key redis key
     * @return 数值
     */
    Long incr(String key);

    /**
     * 自减
     *
     * @param key redis key
     * @return 数值
     */
    Long decr(String key);

    /**
     * 增加并获取分数
     *
     * @param key   键
     * @param score 分数
     * @param value 值
     * @return 结果，得分
     */
    Double zincrby(String key, Double score, String value);

    /**
     * 增加分数
     *
     * @param key   键
     * @param score 分数
     * @param value 值
     * @return 结果
     */
    Boolean zadd(String key, Double score, String value);

    /**
     * 获取分数
     *
     * @param key   键
     * @param value 值
     * @return 结果，分数
     */
    Double zscore(String key, String value);

    /**
     * 按照分数逆序排序，获取 val-> score 这样的map
     *
     * @param key    值
     * @param offset 偏移量
     * @param count  数量
     * @return 结果，map
     */
    Map<String, Double> valueToScore(String key, final int offset, int count);

    /**
     * 向set集合中添加元素
     *
     * @param key   以key为名
     * @param value 元素
     * @return 结果
     */
    Long sadd(String key, String value);

    /**
     * 从set集合中移除元素
     *
     * @param key   以key为名
     * @param value 元素
     * @return 结果
     */
    Long srem(String key, String value);

    /**
     * 获取集合中的元素个数
     *
     * @param key 键
     * @return 个数
     */
    Long scard(String key);

    /**
     * 判断value是否是key集合中的元素
     *
     * @param key   键
     * @param value 值
     * @return 结果
     */
    Boolean sismember(String key, String value);

    /**
     * 扫描集合中的值
     * Use a Cursor to iterate over elements in sorted set at key.
     *
     * @param key   键
     * @param match pattern模式
     * @return 元组值
     */
    Cursor<RedisZSetCommands.Tuple> zscan(String key, String match);

    /**
     * 以加锁的方式运行 consumer
     *
     * @param key      键
     * @param timeout  超时
     * @param consumer 消费，当为 true 时，获取到了锁
     */
    void doWithLock(String key, int timeout, Consumer<Boolean> consumer);

    /**
     * 获取以键集合
     *
     * @param prefix 前缀
     * @return 键集合
     */
    Set<String> keys(String prefix);
}
