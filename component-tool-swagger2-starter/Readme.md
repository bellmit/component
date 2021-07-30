## Swagger一键集成

（推荐使用`component-tool-knife4j-starter`）

## 使用

引入pom依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-tool-swagger2-starter</artifactId>
    <version>${lyloou.component.version}</version>
</dependency>
```

添加配置

```yaml
component:
  swagger2:
    # 是否启用
    enable: true
    group-name: default
    # 扫描的包，多个包使用逗号隔开
    base-package: com.lyloou.component.keyvalueitem
    contact-name: lyloou
    contact-email: lyloou@qq.com
    contact-url: http://lyloou.com
    description: swagger api
    title: ${spring.application.name} API Documents
    url: http://lyloou.com
    version: 0.0.1
    # 自定义参数配置，可配置N个
    parameter-config:
      - name: Authorization
        description: 公共参数, access_token
        # header, cookie, body, query
        type: header
        data-type: String
        required: false
        # 测试接口时，自动填充token的值
        default-value: "Authorization"
```