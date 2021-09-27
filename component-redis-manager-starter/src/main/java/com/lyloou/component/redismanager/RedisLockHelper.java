package com.lyloou.component.redismanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.concurrent.TimeUnit;

/**
 * https://www.programcreek.com/java-api-examples/?code=micro-jframework%2Fjframework%2Fjframework-master%2Fjframework-redis%2Fsrc%2Fmain%2Fjava%2Fcom%2Fgithub%2Fneatlife%2Fjframework%2Fredis%2Futil%2FLockUtil.java#
 * https://github.com/micro-jframework/jframework/blob/master/jframework-redis/src/main/java/com/github/neatlife/jframework/redis/util/LockUtil.java
 *
 * @author lilou
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class RedisLockHelper {
    private static final String LOCK_SCRIPT_STR = "if redis.call('set',KEYS[1],ARGV[1],'EX',ARGV[2],'NX') then return 1 else return 0 end";
    private static final String UNLOCK_SCRIPT_STR = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static final Integer DEFAULT_EXPIRE_SECOND = 60;
    private static final Long DEFAULT_LOOP_TIMES = 10L;
    private static final Long DEFAULT_SLEEP_INTERVAL = 100L;
    private static final String PACKAGE_NAME_SPLIT_STR = "\\.";
    private static final String CLASS_AND_METHOD_CONCAT_STR = "->";
    private static final Long SUCCESS = 1L;
    private final RedisTemplate redisTemplate;

    public RedisLockHelper(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 得到分布式锁
     * 默认key：调用者类名
     */
    public boolean tryGetDistributedLock() {
        String callerKey = getCurrentThreadCaller();
        String requestId = String.valueOf(Thread.currentThread().getId());
        return tryGetDistributedLock(callerKey, requestId);
    }

    /**
     * @param lockKey   锁名称
     * @param requestId 随机请求id
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId) {
        return tryGetDistributedLock(lockKey, requestId, DEFAULT_EXPIRE_SECOND);
    }

    /**
     * @param lockKey      key
     * @param requestId    随机请求id
     * @param expireSecond 超时秒
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, Integer expireSecond) {
        return tryGetDistributedLock(lockKey, requestId, expireSecond, DEFAULT_LOOP_TIMES, DEFAULT_SLEEP_INTERVAL);
    }


    /**
     * 加锁
     *
     * @param lockKey       key
     * @param requestId     随机请求id
     * @param expireSecond  超时秒
     * @param loopTimes     循环次数
     * @param sleepInterval 等待间隔（毫秒）
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, Integer expireSecond, Long loopTimes, Long sleepInterval) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LOCK_SCRIPT_STR, Long.class);
        while (loopTimes-- >= 0) {
            Object result = redisTemplate.execute(
                    (RedisConnection connection) -> connection.eval(
                            redisScript.getScriptAsString().getBytes(),
                            ReturnType.INTEGER,
                            1,
                            lockKey.getBytes(),
                            requestId.getBytes(),
                            String.valueOf(expireSecond).getBytes()
                    )
            );
            if (SUCCESS.equals(result)) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(sleepInterval);
            } catch (InterruptedException e) {
                log.error("e message: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    /**
     * 释放锁
     */
    public boolean releaseDistributedLock() {
        String callerKey = getCurrentThreadCaller();
        String requestId = String.valueOf(Thread.currentThread().getId());
        return releaseDistributedLock(callerKey, requestId);
    }

    /**
     * 释放锁
     *
     * @param lockKey   key
     * @param requestId 加锁的请求id
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(UNLOCK_SCRIPT_STR, Long.class);
        Object result = redisTemplate.execute(
                (RedisConnection connection) -> connection.eval(
                        redisScript.getScriptAsString().getBytes(),
                        ReturnType.INTEGER,
                        1,
                        lockKey.getBytes(),
                        requestId.getBytes()
                )
        );
        return SUCCESS.equals(result);
    }

    private String getSimpleClassName(String className) {
        String[] splits = className.split(PACKAGE_NAME_SPLIT_STR);
        return splits[splits.length - 1];
    }

    /**
     * Get caller
     */
    private String getCurrentThreadCaller() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return getSimpleClassName(stackTraceElement.getClassName()) + CLASS_AND_METHOD_CONCAT_STR + stackTraceElement.getMethodName();
    }
}