## 组件

### component-dto

描述：数据传输基础模块

使用方法：

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-dto</artifactId>
    <version>${project.version}</version>
</dependency>
```

4. 具体查看 [component-dto](./component-dto/Readme.md)

### component-logger-controller-starter

描述：日志级别管理

使用方法：

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-logger-controller-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

4. 具体查看 [component-logger-controller-starter](./component-logger-controller-starter/Readme.md)

### component-schedule-monitor-starter

描述：AOP 实现定时器方法监控——ScheduleMonitor

使用方法：

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-schedule-monitor-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

4. 具体查看 [component-schedule-monitor-starter](./component-schedule-monitor-starter/Readme.md)

### component-logger-request-statistic-starter

描述： controller 中的api请求信息统计

使用方法：

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-logger-request-statistic-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

2. 修改日志级别

```properties
logging.level.com.lyloou.component.loggerrequeststatistic=INFO
```

3. 在需要统计的 controller 类上，或mapping方法上添加注解

```java

@RestController
// 对整个controller中的api有效
@RequestStatistic
public class PlaylistController extends BaseController {
    // ...
}
```

或者

```java

@RestController
public class HiController extends BaseController {
    // 只对此方法统计
    @RequestStatistic
    @GetMapping("hi")
    public String sayHi() {
        return "hi";
    }
}
```

效果： 当发起请求时，会打印类似信息如下

```ini
【请求信息统计】
==> Request URL: http://localhost:8080/room/admin/playlist/copy
==> Start Time: Fri Mar 26 18:04:50 CST 2021
==> End Time: Fri Mar 26 18:04:50 CST 2021
==> Taken Time: 95ms
==> Replay Curl: CURL -X POST "http://localhost:8080/room/admin/playlist/copy" -H "host:localhost:8080" -H "connection:keep-alive" -H "accept:*/*" -H "origin:http://localhost:8080" -H "user-agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3867.400 QQBrowser/10.7.4315.400" -H "dnt:1" -H "content-type:application/json" -H "referer:http://localhost:8080/room/admin/swagger-ui.html" -H "accept-encoding:gzip, deflate, br" -H "accept-language:zh-CN,zh;q=0.9" -H "cookie:XXL_JOB_LOGIN_IDENTITY=ab" -d "{
  \"playlistId\": 1
}"
```     

哪么就可以用下面的shell命令来重新发起请求了：

```shell script
CURL -X POST "http://localhost:8080/room/admin/playlist/copy" -H "host:localhost:8080" -H "connection:keep-alive" -H "accept:*/*" -H "origin:http://localhost:8080" -H "user-agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3867.400 QQBrowser/10.7.4315.400" -H "dnt:1" -H "content-type:application/json" -H "referer:http://localhost:8080/room/admin/swagger-ui.html" -H "accept-encoding:gzip, deflate, br" -H "accept-language:zh-CN,zh;q=0.9" -H "cookie:XXL_JOB_LOGIN_IDENTITY=ab" -d "{\"playlistId\": 1}"
```

4. 具体查看 [component-logger-request-statistic-starter](./component-logger-request-statistic-starter/Readme.md)

### component-tool-code-generator

描述：mybatis-plus的自成代码功能 使用方法

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-tool-code-generator</artifactId>
    <version>${project.version}</version>
</dependency>
```

2. 修改配置信息`code-generator.properties`
3. 运行 `CodeGenerator.java`的main方法
4. 具体查看 [component-tool-code-generator](./component-tool-code-generator/Readme.md)

### component-redis-manager-starter

描述：redis缓存管理（结合缓存注解使用） 使用方法

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-redis-manager-starter</artifactId>
    <version>${project.version}</version>
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

4. 具体查看 [component-redis-manager-starter](./component-redis-manager-starter/Readme.md)