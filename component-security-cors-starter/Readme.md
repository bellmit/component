## CORS 跨源解决方案

简单使用，引入 starter 包即可支持。

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-security-cors-starter</artifactId>
    <version>${lyloou.component.version}</version>
</dependency>
```

默认配置是

```yaml
component:
  cors:
    allowCredentials: true
    allowedOrigins: "*"
    allowedMethods: "*"
    allowedHeaders: "*"
    maxAge: 1800
```

也可以自定义配置

```yaml
component:
  cors:
    allowCredentials: true
    allowedOrigins:
      - "http://127.0.0.1:8081"
      - "http://127.0.0.1:8082"
    allowedMethods:
      - "GET"
      - "POST"
    allowedHeaders: "*"
    maxAge: 1800
```