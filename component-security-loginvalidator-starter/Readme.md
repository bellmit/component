# 基于AOP和JWT实现的Token身份认证组件

## 客户端使用

在 Header 中配置身份认证 Token 的信息：
如：`Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Mjg4MzQ3MjQsImV4cCI6MTYyOTQzOTUyNCwieC11c2VyLW5hbWUiOiJhYmNkZSIsIngtdXNlci1pZCI6IjEifQ.x0nIhSUPfxC5FlnzJ-MmJvLnJv7w5ZvFzGlNphdSByE`

## 服务端使用方式

添加依赖

```xml

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
