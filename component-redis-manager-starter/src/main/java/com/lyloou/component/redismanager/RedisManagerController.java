package com.lyloou.component.redismanager;

import com.github.pagehelper.PageInfo;
import com.lyloou.component.dto.MultiResponse;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "【component】缓存-Redis缓存管理接口")
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

    @GetMapping("/page")
    @ApiOperation(value = "根据前缀分页获取键值对")
    public SingleResponse<PageInfo<PrefixPageResponse>> page(PrefixPageQuery qry) {
        return SingleResponse.buildSuccess(redisManagerService.page(qry));
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
        redisManagerService.delByPrefixAndKey(prefix, key);
        return SingleResponse.buildSuccess();
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
        redisManagerService.expire(prefix, key, ttl);
        return SingleResponse.buildSuccess();
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
        AssertUtil.notNullParam(key, "key is invalid");

        redisManagerService.delByWrapKey(key);
        return SingleResponse.buildSuccess();
    }
}
