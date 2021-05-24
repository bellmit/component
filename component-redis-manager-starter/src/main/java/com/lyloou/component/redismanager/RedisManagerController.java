package com.lyloou.component.redismanager;

import com.lyloou.component.dto.MultiResponse;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * <p>监控管理api</p>
 *
 * @author lilou
 * @since 2021/3/7
 */
@RestController
@Api(tags = "redis缓存管理")
@RequestMapping("/redismanager")
public class RedisManagerController {

    @Autowired
    RedisManagerService redisManagerService;

    /**
     * 获取本项目中所有可控的key前缀前缀
     *
     * @return 结果
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取本项目中所有可控的key前缀前缀")
    public SingleResponse<Map<String, Map<String, String>>> list() {
        return SingleResponse.buildSuccess(redisManagerService.list());
    }

    /**
     * 根据前缀获取所有的 key
     *
     * @param prefix 前缀
     * @return 结果
     */
    @GetMapping("/keys")
    @ApiOperation(value = "根据前缀获取所有的 key")
    public MultiResponse<Set<String>, String> keys(String prefix) {
        return MultiResponse.buildSuccess(redisManagerService.keys(prefix));
    }

    /**
     * 删除缓存
     *
     * @param prefix 前缀
     * @param key    键
     * @return 结果
     */
    @GetMapping("/del")
    @ApiOperation(value = "删除缓存")
    public SingleResponse<String> del(@RequestParam(required = true) String prefix,
                                      @RequestParam(required = false) String key) {
        final boolean result = redisManagerService.del(prefix, key);
        return SingleResponse.buildSuccess(String.format("删除缓存：%s::%s，结果:%s", prefix, key, result));
    }

    /**
     * 设置key过期时间
     * 也可以通过 spring.redis.cache-config-list 来单独配置
     *
     * @param prefix 前缀
     * @param key    键
     * @param ttl    超时
     * @return 结果
     */
    @GetMapping("/expire")
    @ApiOperation(value = "设置key过期时间")
    public SingleResponse<String> expire(@RequestParam(required = true) String prefix,
                                         @RequestParam(required = false) String key,
                                         @RequestParam(required = false) Integer ttl
    ) {
        final boolean result = redisManagerService.expire(prefix, key, ttl);
        return SingleResponse.buildSuccess(String.format("设置缓存过期：%s::%s，ttl:%s, 结果:%s", prefix, key, ttl, result));
    }

    /**
     * 根据具体的key删除缓存
     *
     * @param key 键
     * @return 结果
     */
    @GetMapping("/delKey")
    @ApiOperation(value = "根据具体的key删除缓存")
    public SingleResponse<String> del(String key) {
        if (Strings.isEmpty(key)) {
            return SingleResponse.buildFailure(CommonCodeMessage.ILLEGAL_PARAM.appendMessage("key is invalid"));
        }

        final boolean result = redisManagerService.del(key);
        return SingleResponse.buildSuccess(String.format("删除缓存：%s，结果:%s", key, result));
    }
}
