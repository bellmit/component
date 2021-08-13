# 基于AOP和JWT实现的Token身份认证组件

## 原理

> 基于 AOP 面向切面编程，在执行前后插入身份认证的逻辑。

**原理细节：**

- 登录过程：这个过程比较简单，将用户 id、用户名、过期时间等属性结合 jwt 工具生成 token，并将用户的信息存入到缓存中，以供后期使用。

- 验证过程：前端通过 Header 头信息的 Authorization 属性得到 Token，先进行 token 验证，再结合缓存验证，验证成功的话，将用户 id 和用户名等信息存入 `ThreadLocal`
  中，这样在执行切面逻辑的时候。就可以从 `ThreadLocal` 中获取数据了，如`UserManager.getUserId()`；执行完成后需要清除 ThreadLocal 中的数据；代码如下

```java
class ValidateLoginAspect {

  @Around("pointCutMethod()")
  public Object preHandle(ProceedingJoinPoint pjp) {
    // ......
    UserContextHolder.getInstance().setContext(userMap);
    final Object proceed;
    try {
      proceed = pjp.proceed();
    } finally {
      UserContextHolder.getInstance().clear();
    }
    return proceed;
  }
}
```

- 认证接口的范围：给 `BaseTokenController`这个基类添加 `@ValidateLogin`， 可以实现一个效果，只要自己的 `Controller` 继承了`BaseTokenController`
  ，那么就不用再声明`@ValidateLogin`注解，自定义`Controller`中的 mapping 都需要身份认证。 （这样就免去了繁琐配置：在拦截器中通过通配符的方式配置哪些接口需要拦截，哪些接口需要放行）

## 服务端使用方式

**添加依赖**

```xml

<dependency>
  <groupId>com.lyloou</groupId>
  <artifactId>component-security-loginvalidator-starter</artifactId>
  <version>${lyloou.component.version}</version>
</dependency>
```

1. 继承`BaseTokenController`类。 因为这个类被`@ValidateLogin`标记，所以其下的所有子类都需要身份认证（具体实现细节，查看`ValidateLoginAspect`）。

```java

@RestController
public class UserController extends BaseTokenController {

    // 从父类继承了ValidateLogin，需要身份验证
    @GetMapping("/ping")
    public String ping() {
        final Integer userId = currentUserId();
        System.out.println(userId);
        return "pong";
    }

}
```

2. 如果继承了 `BaseTokenController` 类，又希望其中的某个方法不要被拦截，可以在方法上标记 `@IgnoreValidateLogin`

```java

@RestController
public class UserController extends BaseTokenController {

    @Autowired
    TokenService tokenService;

    // 手动忽略身份验证
    @IgnoreValidateLogin
    @GetMapping("/login")
    public String login(String userId, String username) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", username);
        map.put("userAvatar", "http://cdn.lyloou.com/a.jpg");

        final String token = tokenService.createToken(userId, username, JSONUtil.toJsonStr(map));
        return token;
    }
}
```

3. 如果没有继承 `BaseTokenController`，又希望在其中某个方法中做身份认证，获取用户 id，可以在方法上标记`@ValidateLogin`

```java

@RestController
public class UserController {


    // 手动添加身份验证
    @ValidateLogin
    @GetMapping("userinfo")
    public Map<String, String> userInfo() {
        Map<String, String> map = new HashMap<>();
        map.put(UserManager.X_USER_ID, UserManager.getUserId() + "");
        map.put(UserManager.X_USER_IP, UserManager.getUserIP());
        map.put(UserManager.X_USER_NAME, UserManager.getUserName());
        map.put(UserManager.X_USER_INFO, UserManager.getUserInfo());
        return map;
    }
}
```

## 使用自定义的缓存

默认使用了内存缓存`ConcurrentHashMap`（单机版本的）

也可以自定义缓存

```java
/**
 * @author lilou
 * @since 2021/7/14
 */
@Service
public class RedisCodeCache implements DataCache {

    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenProperties tokenProperties

    @Override
    public void set(String key, String value) {
        set(key, value, tokenProperties.getExpireSecond());
    }

    @Override
    public void set(String key, String value, long timeout) {
        redisService.set(key, value, (int) timeout);
    }

  @Override
  public String get(String key) {
    return redisService.get(key);
  }

  @Override
  public void remove(String key) {
    redisService.del(key);
  }

  @Override
  public boolean containsKey(String key) {
    return redisService.exists(key);
  }
}
```

## 客户端使用

在 Header 中配置身份认证 Token 的信息：
如：`Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Mjg4MzQ3MjQsImV4cCI6MTYyOTQzOTUyNCwieC11c2VyLW5hbWUiOiJhYmNkZSIsIngtdXNlci1pZCI6IjEifQ.x0nIhSUPfxC5FlnzJ-MmJvLnJv7w5ZvFzGlNphdSByE`

## 源码实现

[component/component-security-loginvalidator-starter at master · lyloou/component](https://github.com/lyloou/component/tree/master/component-security-loginvalidator-starter)
