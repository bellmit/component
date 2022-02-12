package com.lyloou.component.redismanager;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.support.NullValue;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Service
@Qualifier("redisService")
public class RedisServiceImpl implements RedisService {
    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Qualifier("jackson2JsonRedisSerializer")
    @Autowired
    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer;

    private RedisLockHelper redisLockHelper;

    @PostConstruct
    public void init() {
        this.redisLockHelper = new RedisLockHelper(redisTemplate);
    }

    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public void set(String key, String value) {
        set(key, value, 0);
    }

    @Override
    public void set(final String key, final String value, final int ttl) {
        Objects.requireNonNull(key, "key should not be null");
        if (Objects.isNull(value)) {
            set(key.getBytes(), null, ttl);
            return;
        }
        set(key.getBytes(), value.getBytes(), ttl);
    }

    @Override
    public String get(final String key) {
        Objects.requireNonNull(key, "key should not be null");
        byte[] bytes = get(key.getBytes());
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    @Override
    public <T extends Serializable> void set(String key, T entity) {
        set(key, entity, 0);
    }

    @Override
    public <T extends Serializable> void set(final String key, final T entity, final int ttl) {
        Objects.requireNonNull(key, "key should not be null");
        try {
            set(key.getBytes(), jackson2JsonRedisSerializer.serialize(entity), ttl);
        } catch (Exception e) {
            log.error("serialize entity error", e);
        }
    }

    @Override
    public <T extends Serializable> T getEntity(final String key) {
        Objects.requireNonNull(key, "key should not be null");
        byte[] bytes = get(key.getBytes());
        if (bytes == null) {
            return null;
        }

        try {
            final Object data = jackson2JsonRedisSerializer.deserialize(bytes);
            //noinspection unchecked
            return (T) data;
        } catch (Exception e) {
            log.warn("deserialize failed, str：" + new String(bytes), e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean exists(final String key) {
        Objects.requireNonNull(key, "key should not be null");
        return exists(key.getBytes());
    }

    @Override
    public void del(String key) {
        Objects.requireNonNull(key, "key should not be null");
        del(key.getBytes());
    }


    @Override
    public Long ttl(final String key) {
        Objects.requireNonNull(key, "key should not be null");
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
        redisTemplate.execute((RedisCallback<Boolean>) con -> {
            if (value == null) {
                byte[] serialize = jackson2JsonRedisSerializer.serialize(null);
                return con.setEx(key, 3 * 60, serialize);
            }

            if (ttl <= 0) {
                return con.set(key, value);
            } else {
                return con.setEx(key, ttl, value);
            }
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

    @Override
    public Map<String, Object> multiGet(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return new HashMap<>();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : keys) {
            final Object value = getEntity(key);
            map.put(key, value);
        }
        return map;
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
        AssertUtil.isTrue(StrUtil.isNotBlank(keyPattern), "keyPattern should not be null or empty");
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
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.zAdd(key.getBytes(), score, value.getBytes()));
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
    public void doWithLock(String key, int timeout, Consumer<Boolean> consumer) {
        // 使用雪花算法生成的id作为默认的requestId
        String requestId = IdUtil.getSnowflakeNextIdStr();
        try {
            final boolean locked = redisLockHelper.tryGetDistributedLock(key, requestId, timeout);
            consumer.accept(locked);
        } finally {
            redisLockHelper.releaseDistributedLock(key, requestId);
        }
    }

    @Override
    public Set<String> keys(String prefix) {
        return redisTemplate.keys(prefix);
    }


    @SneakyThrows
    public <T> List<T> cacheList(String key, int ttl, Supplier<List<T>> supplier) {
        final String dataStr = get(key);
        if (Objects.nonNull(dataStr)) {
            List<T> bean = OM.readValue(dataStr, new TypeReference<List<T>>() {
            });
            if (bean != null) {
                return bean;
            }
        }

        final List<T> data = supplier.get();
        set(key, OM.writeValueAsString(data), ttl);
        return data;
    }

    @SneakyThrows
    public <T> T cacheObject(String key, int ttl, Class<T> clazz, Supplier<T> supplier) {
        final String dataStr = get(key);
        if (Objects.nonNull(dataStr)) {
            final T bean = OM.readValue(dataStr, clazz);
            if (bean != null) {
                return bean;
            }
        }
        final T data = supplier.get();
        String value = OM.writeValueAsString(data);
        set(key, value, ttl);
        return data;
    }
}
