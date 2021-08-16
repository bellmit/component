package com.lyloou.component.redismanager;


import com.github.pagehelper.PageInfo;
import com.lyloou.component.dto.PageInfoHelper;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 监控处理类
 *
 * @author lilou
 * @since 2021/3/7
 */
@Service
@Slf4j
public class RedisManagerService {

    @Value("${server.port:8080}")
    private int port;

    @Autowired
    private RedisService redisService;

    public static final String SEP = CacheKeyPrefix.SEPARATOR;
    public static final String STAR = "*";
    public static final String SEP_STAR = SEP + STAR;
    public static final Integer DEFAULT_TTL = 300;

    /**
     * 存放任务的状态的map
     */
    private final Map<String, Boolean> cachePrefixMap = new ConcurrentHashMap<>();


    public Map<String, Map<String, String>> list() {
        Map<String, Map<String, String>> map = new HashMap<>();
        this.cachePrefixMap.forEach((prefix, aBoolean) -> {
            map.put(prefix, getOperationMap(prefix));
        });
        return map;
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    private Map<String, String> getOperationMap(String prefix) {
        String prefixUrl = "";
        final HttpServletRequest request = getRequest();
        if (request != null) {
            try {
                final String hostAddress = Inet4Address.getLocalHost().getHostAddress();
                final String uri = request.getRequestURI();
                prefixUrl = "http://" + hostAddress + ":" + port + uri.substring(0, uri.lastIndexOf("/"));
            } catch (UnknownHostException e) {
                // ignore
            }
        }

        Map<String, String> map = new LinkedHashMap<>(4);
        map.put("1. 获取所有键", prefixUrl.concat("/keys?prefix=").concat(prefix));
        map.put("2. 根据prefix和key删除", prefixUrl.concat("/del?prefix=").concat(prefix).concat("&key="));
        map.put("3. 设置过期", prefixUrl.concat("/expire?prefix=").concat(prefix).concat("&key=").concat("&ttl="));
        map.put("4. 根据具体key删除", prefixUrl.concat("/delKey?key=").concat(prefix));
        return map;
    }

    /**
     * 检查当前项目的key前缀，只可操作本项目中的key（防止乱删除）
     */
    public void checkProjectPrefix(String prefix) {
        AssertUtil.notNullParam(prefix, "prefix不能为空");

        final boolean noneMatch = cachePrefixMap.keySet().stream()
                .noneMatch(prefix::startsWith);
        AssertUtil.isFalse(noneMatch, "只可操作本项目中的key");
    }

    public void unregisterCachePrefix(String cachePrefix) {
        cachePrefixMap.remove(cachePrefix);
    }

    public void registerCachePrefix(String cachePrefix) {
        cachePrefixMap.put(cachePrefix, true);
    }


    // 手动调用删除方法
    public void delByWrapKey(String wrapKey) {
        final String[] prefixKeyArray = wrapKey.split(SEP);
        checkProjectPrefix(prefixKeyArray[0]);
        AssertUtil.isTrue(redisService.exists(wrapKey), "key不存在");

        redisService.del(wrapKey);
    }

    public void delByPrefixAndKey(@NonNull String prefix, @Nullable String key) {

        checkProjectPrefix(prefix);

        if (Strings.isEmpty(key)) {
            redisService.delPattern(prefix + SEP_STAR);
            return;
        }

        final String wrapKey = prefix + SEP + key;
        AssertUtil.isTrue(redisService.exists(wrapKey), "key不存在");
        redisService.del(wrapKey.getBytes());
    }

    public void expire(String prefix, String key, Integer ttl) {
        checkProjectPrefix(prefix);

        String wrapKey = prefix;
        if (Strings.isNotEmpty(key)) {
            wrapKey = wrapKey + SEP + key;
        }
        AssertUtil.isTrue(redisService.exists(wrapKey), "key不存在");

        if (ttl == null) {
            ttl = DEFAULT_TTL;
        }
        redisService.expire(ttl, wrapKey);
    }

    public Set<String> keys(String prefix) {
        checkProjectPrefix(prefix);
        return redisService.keys(prefix + SEP_STAR);
    }

    public PageInfo<PrefixPageResponse> page(PrefixPageQuery qry) {
        checkProjectPrefix(qry.getPrefix());

        final Set<String> keys = redisService.keys(qry.getPrefix() + SEP_STAR);
        final Set<String> sortedSet = new TreeSet<>(keys);
        final List<String> limitKeys = sortedSet.stream()
                .skip(qry.getOffset())
                .limit(qry.getPageSize())
                .collect(Collectors.toList());


        final Map<String, Object> multiGet = redisService.multiGet(limitKeys);
        List<PrefixPageResponse> list = new ArrayList<>(multiGet.size());
        multiGet.forEach((k, v) -> {
            final PrefixPageResponse item = new PrefixPageResponse();
            item.setKey(k);
            item.setValue(v);
            list.add(item);
        });
        final PageInfo pageInfo = PageInfoHelper.getPageInfo(new ArrayList<>(list));
        pageInfo.setTotal(keys.size());
        pageInfo.setPageNum(qry.getPageNum());
        pageInfo.setPageSize(qry.getPageSize());
        return pageInfo;
    }
}
