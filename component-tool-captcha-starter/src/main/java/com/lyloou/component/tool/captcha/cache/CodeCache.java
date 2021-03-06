package com.lyloou.component.tool.captcha.cache;

/**
 * 验证码缓存，用来缓存code
 * 默认使用 CodeDefaultCache，可以使用 Redis 来自定义实现（Redis无需手动管理过期key）
 *
 * @author lilou
 * @since 2021/7/6
 */

public interface CodeCache {
    /**
     * 设置缓存
     *
     * @param key   缓存KEY
     * @param value 缓存内容
     */
    void set(String key, String value);

    /**
     * 设置缓存，指定过期时间
     *
     * @param key     缓存KEY
     * @param value   缓存内容
     * @param timeout 指定缓存过期时间（毫秒）
     */
    void set(String key, String value, long timeout);

    /**
     * 获取缓存
     *
     * @param key 缓存KEY
     * @return 缓存内容
     */
    String get(String key);

    /**
     * 移除key，和对应值
     *
     * @param key 缓存KEY
     */
    void remove(String key);

    /**
     * 是否存在key，如果对应key的value值已过期，也返回false
     *
     * @param key 缓存KEY
     * @return true：存在key，并且value没过期；false：key不存在或者已过期
     */
    boolean containsKey(String key);

    /**
     * 清理过期的缓存
     */
    default void pruneCache() {
    }
}
