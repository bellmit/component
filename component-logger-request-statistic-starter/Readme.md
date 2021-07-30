描述： controller 中的api请求信息统计

使用方法：

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-logger-request-statistic-starter</artifactId>
    <version>${lyloou.component.version}</version>
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
