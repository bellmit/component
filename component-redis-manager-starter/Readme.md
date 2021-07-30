# redis缓存管理组件

## 简单使用

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-redis-manager-starter</artifactId>
    <version>${lyloou.component.version}</version>
</dependency>
```

2. 添加redis配置

```yml
spring:
  redis:
    host: 127.0.0.1
    password: ''
    port: 6379
    cache-null-values: true  # 是否缓存null值
    ttl: 611          # 过期时间
```

3. 结合 `@Cacheable` 、`@Caching`、`@CacheEvict` 来管理缓存

## 一、这个技术解决了什么问题？

1. 有效管理缓存服务，当项目拆分过多，缓存清理不方便时，可以通过本组件提供的刷新api来方便管理

主要可以参考 `RedisManagerController` 和 `RedisManagerService`

2. 提供了常用的 redis 封装。

主要可以参考 `RedisService`，如：

- set、del、expire、incr、decr、zincrby、zadd、zscore、keys等等操作
- lock (redis 分页式锁)

## 二、它的优势是什么？

解耦合，侵入性低。引入依赖，按照缓存规则使用即可加入管理。

通过配置`cache-config-list`，可以单独设置key的过期时间。

封装最常用的api，引入了这个依赖，基本可以掌控 redis 相关操作(当然可以修改升级)。

灵活，需要其它实现时，可以覆盖 `RedisManagerAutoConfiguration` 中的 `bean`。

## 三、它的劣势是什么？

只有被标记了 `@Cacheable`、`@Caching`等注解的key，才可以被管理。

当然，也可以手动加入进去，按照规则命名，也可以实现被管理，如：

```java
import com.lyloou.component.redismanager.RedisManagerService;
import com.lyloou.component.redismanager.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.CacheKeyPrefix;

@Service
public class Demo {
    @Autowired
    RedisService redisService;

    @Autowired
    RedisManagerService redisManagerService;

    // 加入管理
    public void addCacheName() {
        redisManagerService.registerCachePrefix("com.lyloou.PersonCo_ID");
    }

    // 添加缓存
    public void addDataToCache() {
        String key = "com.lyloou.PersonCo" + CacheKeyPrefix.SEPARATOR + 1;
        redisService.set(key);
    }
}
```

另外为了安全考虑，只有被注册过的前缀才可以操作（特别是后台服务修改，需要更新前端服务生成的缓存时会用到；也可以直接调用前端服务的删除缓存api）

```java
public interface CachePrefix {
    String FytInfoForTvCo_CHANNEL = "com.lyloou.fyt.dto.clientobject.tv.FytInfoForTvCo.channel";
    String PlaylistForTvCo_ID = "com.lyloou.fyt.dto.clientobject.tv.PlaylistForTvCo.id";
    String KeyValueItemCo_NAME_KEY = "com.lyloou.fyt.dto.clientobject.KeyValueItemCo.itemName.itemKey";

    public static void main(String[] args) throws IllegalAccessException {
        System.out.println(listAllCachePrefix());
        System.out.println(FytInfoForTvCo_CHANNEL);
    }

    public static List<String> listAllCachePrefix() {
        return Arrays.stream(CachePrefix.class.getDeclaredFields())
                .map(field -> {
                    try {
                        return field.get(field.getName());
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CachePrefixRegister {
    private final RedisManagerService redisManagerService;

    @PostConstruct
    public void doRegistration() {
        final List<String> cachePrefixList = CachePrefix.listAllCachePrefix();
        for (String cachePrefix : cachePrefixList) {
            redisManagerService.registerCachePrefix(cachePrefix);
        }
    }
}

```

## 四、它和 XX 技术有什么不一样？

**refresh方案**
这种管理方案是通过在url后面加 ?refresh=1，当为1的时候去刷新，当为其它值时不刷新。

    如下：
    当请求：http://localhost:8082/api/user/1?refresh=1 时，强制走数据库。
    当请求：http://localhost:8082/api/user/1 时，有缓存时走缓存，没缓存时走db。

refresh方案不便管理，不好排查，侵入性太强

## 五、在什么场景下可以使用它？

需要用到redis缓存的地方，都可以用到。

## 六、如何正确地使用它？

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-redis-manager-starter</artifactId>
    <version>${lyloou.component.version}</version>
</dependency>
```

2. 添加redis配置

```yml
spring:
  redis:
    host: 127.0.0.1
    password: ''
    port: 6379
    cache-null-values: true # 是否缓存null值，防止缓存穿透（默认true）
    ttl: 611 # 过期时间 （默认300）
    cache-config-list: # 单独指定一些 cache-name
      - cache-name: com.lyloou.component.redismanager.user
        cache-null-values: false
        ttl: 888
      - cache-name: com.lyloou.component.redismanager.person
        cache-null-values: true
        ttl: 998
```

3. 结合 `@Cacheable` 、`@Caching`、`@CacheEvict` 来管理缓存

```java
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

// 统一管理 cacheName 名，
// 以实体类的全限定名+要缓存的属性作为 cacheName
// 所有的 cacheName 都定义在这个类中
public interface CacheNames {
    // 根据id来缓存用户
    String UserCo_ID = "com.lyloou.UserCo.id";
    // 缓存用户排名（不需要键，只需要 cacheName 即可）
    String UserRankingInfo = "com.lyloou.UserRankingInfo";
}

// 需要缓存的地方
@Service
public class UserApiServiceImpl implements UserApiService {
    @Override
    @Cacheable(cacheNames = CacheNames.UserCo_ID, key = "#qry.userId", sync = true)
    public UserCo getPlaylist(UserGetQry qry) {
        final Optional<User> userOptional = userService.getUserOptional(qry);
        if (!userOptional.isPresent()) {
            return null;
        }
        UserCo userCo = new UserCo();
        BeanUtils.copyProperties(userOptional.get(), userCo);
        return userCo;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.UserRankingInfo, sync = true)
    public UserRankingInfo getUserRankingInfo(UserRankingInfoGetQry qry) {
        final Optional<UserRankingInfo> userRankingInfoOptional = userService.getUserRankingInfo(qry);
        return userRankingInfoOptional.orElse(null);
    }
}

// 需要更新缓存的地方
@Service
public class UserAdminServiceImpl implements UserAdminService {

    /**
     * 对用户做修改需要刷新缓存：当前用户的缓存、用户排行缓存
     *
     * @param userCo 传递过来的用户信息
     * @return 更新结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.UserCo_ID, key = "#user.id"),
            @CacheEvict(value = CacheNames.UserRankingInfo, allEntries = true)
    })
    public Boolean updateUser(UserCo userCo) {
        User user = new User();
        BeanUtils.copyProperties(userCo, user);
        return userRepository.updateUser(user);
    }
}

```

4. 通过api查看和修改缓存（如下）

```shell
curl -X PATCH "http://localhost:8082/api/redismanager/keys?prefix=com.lyloou.UserCo.id" -H "accept: */*"   
# 结果如下
{
  "code": 10000,
  "msg": "success",
  "data": [
    "com.lyloou.UserCo.id::12",
    "com.lyloou.UserCo.id::1"
  ]
}


   
# 列出所有可控制的前缀   
curl -X GET "http://localhost:8082/api/redismanager/list" -H "accept: */*"
# 结果如下
{
  "code": 10000,
  "msg": "success",
  "data": {
    "com.lyloou.UserCo.id": true,
    "com.lyloou.UserRankingInfo": true
  }
}

# 根据cacheName 和 key 来删除
curl -X GET "http://localhost:8082/api/redismanager/del?key=2&prefix=com.lyloou.UserCo.id" -H "accept: */*"
# 显示如下
{
  "code": 10000,
  "msg": "success",
  "data": "删除缓存：com.lyloou.UserCo.id::2，结果:true"
}

# 设置过期时间
curl -X GET "http://localhost:8082/api/redismanager/expire?key=2&prefix=com.lyloou.UserCo.id&ttl=500" -H "accept: */*"
# 显示如下
{
  "code": 10000,
  "msg": "success",
  "data": "设置缓存过期：com.lyloou.UserCo.id::2，ttl:500, 结果:true"
}

# 根据具体的key删除，"%3A" 表示":"
curl -X GET "http://localhost:8082/api/redismanager/delKey?key=com.lyloou.UserCo.id%3A%3A1" -H "accept: */*"
# 结果如下
{
  "code": 10000,
  "msg": "success",
  "data": "删除缓存：com.lyloou.UserCo.id::1，结果:true"
}
```

A类中的方法，调用A类中的另一个缓存过的方法时缓存不生效问题。 aop的限制，通过代理的方式调用即可。

## 七、它的实现原理是什么？

1. 项目启动时，扫描带有下面这几个注解的方法或类，提取出注解中配置的`cacheNames`值，并添加到 map 容器中，管理（see RedisManagerService#prefixMap）

```
@Cacheable
@Caching
@CacheConfig
```

2. RedisManagerService 中提供简单的删除缓存功能、列出本项目中用到的key前缀（cacheName）功能等等；

4. 通过 controller 暴露出必要的api，可以对缓存进行控制（其它项目调用此api即可）。

