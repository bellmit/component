package com.lyloou.component.redismanager;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Service
@Qualifier("redisService")
public class RedisServiceImpl implements RedisService {

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Qualifier("jackson2JsonRedisSerializer")
    @Autowired
    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer;

    @Override
    public void set(String key, String value) {
        set(key.getBytes(), value.getBytes());
    }

    @Override
    public <T extends Serializable> void set(String key, T entity) {
        try {
            set(key.getBytes(), jackson2JsonRedisSerializer.serialize(entity));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String get(final String key) {
        byte[] bytes = get(key.getBytes());
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    @Override
    public <T extends Serializable> T getEntity(final String key) {
        try {
            byte[] bytes = get(key.getBytes());
            if (bytes == null) {
                return null;
            }
            final Object data = jackson2JsonRedisSerializer.deserialize(bytes);
            //noinspection unchecked
            return (T) data;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean exists(final String key) {
        return exists(key.getBytes());
    }

    @Override
    public void del(String key) {
        del(key.getBytes());
    }

    @Override
    public void set(final String key, final String value, final int ttl) {
        set(key.getBytes(), value.getBytes(), ttl);
    }

    @Override
    public <T extends Serializable> void set(final String key, final T entity, final int ttl) {
        try {
            set(key.getBytes(), jackson2JsonRedisSerializer.serialize(entity), ttl);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Long ttl(final String key) {
        return ttl(key.getBytes());
    }

    @Override
    public void expire(final int seconds, final String... keys) {
        byte[][] bytes = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            bytes[i] = keys[i].getBytes();
        }
        expire(seconds, bytes);
    }

    private void set(final byte[] key, final byte[] value, final int ttl) {
        redisTemplate.execute((RedisCallback<Void>) con -> {
            con.set(key, value);
            if (ttl != 0) {
                con.expire(key, ttl);
            }
            return null;
        });
    }

    @Override
    public void set(byte[] key, byte[] value) {
        set(key, value, 0);
    }

    @Override
    public byte[] get(final byte[] key) {
        return redisTemplate.execute((RedisCallback<byte[]>) con -> con.get(key));
    }

    private Long ttl(final byte[] key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.ttl(key));
    }


    private void expire(final int seconds, final byte[]... keys) {
        redisTemplate.execute((RedisCallback<Void>) con -> {
            for (byte[] key : keys) {
                con.expire(key, seconds);
            }
            return null;
        });
    }

    @Override
    public Boolean exists(final byte[] key) {
        return redisTemplate.execute((RedisCallback<Boolean>) con -> con.exists(key));
    }

    @Override
    public void del(final byte[]... key) {
        redisTemplate.execute((RedisCallback<Void>) con -> {
            con.del(key);
            return null;
        });
    }

    @Override
    public void delPattern(String keyPattern) {
        del(keys(keyPattern.getBytes()));
    }

    private Long incr(final byte[] key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.incr(key));
    }

    @Override
    public Long incr(String key) {
        return incr(key.getBytes());
    }

    @Override
    public Long decr(String key) {
        return decr(key.getBytes());
    }


    private Long decr(final byte[] key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.decr(key));
    }

    private byte[][] keys(final byte[] keys) {
        return redisTemplate.execute((RedisCallback<byte[][]>) con -> {
            Set<byte[]> keySet = con.keys(keys);
            if (keySet == null) {
                return null;
            }

            byte[][] tmp = new byte[keySet.size()][];
            int i = 0;
            for (byte[] e : keySet) {
                tmp[i++] = e;
            }
            return tmp;
        });
    }

    @Override
    public Double zincrby(String key, Double score, String value) {
        return redisTemplate.execute((RedisCallback<Double>) redisConnection -> {
            Double dScore = score;
            dScore = redisConnection.zIncrBy(key.getBytes(), dScore, value.getBytes());
            return dScore;
        });
    }

    @Override
    public Boolean zadd(String key, Double score, String value) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection ->
                redisConnection.zAdd(key.getBytes(), score, value.getBytes()));
    }

    @Override
    public Double zscore(String key, String value) {
        return redisTemplate.execute((RedisCallback<Double>) redisConnection -> {

            Double score = redisConnection.zScore(key.getBytes(), value.getBytes());
            if (score != null) {
                return score;
            }
            return null;
        });
    }

    @Override
    public Map<String, Double> valueToScore(final String key, final int offset, final int count) {
        return redisTemplate.execute((RedisCallback<Map<String, Double>>) redisConnection -> {
            Map<String, Double> map = new HashMap<>();
            final RedisZSetCommands.Range range = new RedisZSetCommands.Range();
            final RedisZSetCommands.Limit limit = new RedisZSetCommands.Limit().offset(offset).count(count);
            final Set<byte[]> values = redisConnection.zRevRangeByScore(key.getBytes(), range, limit);
            if (values == null) {
                return map;
            }

            for (byte[] val : values) {
                Double score = redisConnection.zScore(key.getBytes(), val);
                if (score == null) {
                    score = 0.0;
                }
                map.put(new String(val), score);
            }
            return map;
        });
    }

    @Override
    public Long sadd(String key, String value) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.sAdd(key.getBytes(), value.getBytes()));
    }

    @Override
    public Long srem(String key, String value) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.sRem(key.getBytes(), value.getBytes()));
    }

    @Override
    public Long scard(String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.sCard(key.getBytes()));
    }

    @Override
    public Boolean sismember(String key, String value) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.sIsMember(key.getBytes(), value.getBytes()));
    }

    @Override
    public Cursor<RedisZSetCommands.Tuple> zscan(String key, String match) {
        return redisTemplate.execute(new RedisCallback<Cursor<RedisZSetCommands.Tuple>>() {
            final ScanOptions options = ScanOptions.scanOptions().match(match).count(1000).build();

            @Override
            public Cursor<RedisZSetCommands.Tuple> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.zScan(key.getBytes(), options);
            }
        });
    }


    @Override
    public boolean lock(String key, int timeout) {
        final long now = System.currentTimeMillis();
        // 获取【新锁的过期时间】
        String newExpireTime = now + timeout + "";
        final Boolean result = redisTemplate.opsForValue().setIfAbsent(key, newExpireTime);
        if (result != null && result) {
            return true;
        }

        // 获取【旧锁的过期时间】
        String oldExpiredTime = (String) Objects.requireNonNull(redisTemplate.opsForValue().get(key));

        // 不可相同，不可重入
        if (Objects.equals(oldExpiredTime, newExpireTime)) {
            return false;
        }

        // 新锁过期比老锁过期还早
        if (Long.parseLong(newExpireTime) < Long.parseLong(oldExpiredTime)) {
            return false;
        }

        //如果锁过期(根据设置的时间来判断过期)
        //oldExpiredTime=A   这两个线程的value都是B  其中一个线程拿到锁
        if (Long.parseLong(oldExpiredTime) < System.currentTimeMillis()) {
            // getAndSet线程安全所以只会有一个线程重新设置锁的新值
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, newExpireTime);
            // 比较锁的getSet获取到的最近锁值和最开始获取到的锁值，如果不相等则证明锁已经被其他线程获取了。
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(oldExpiredTime)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void unlock(String key) {
        try {
            del(key);
        } catch (Exception e) {
            log.error("释放分布式锁失败,key = {}", key, e);
        }
        log.debug("释放分布式锁,key = {}", key);
    }

    @Override
    public void doWithLock(String key, int timeout, Consumer<Boolean> consumer) {
        boolean lock = false;
        try {
            lock = lock(key, timeout);
            consumer.accept(lock);
        } finally {
            if (lock) {
                unlock(key);
            }
        }
    }

    @Override
    public Set<String> keys(String prefix) {
        return redisTemplate.keys(prefix);
    }

}
