## [knife4j](https://doc.xiaominfo.com/knife4j/documentation/get_start.html)

- Swagger的UI增强版
- Swagger接口文档服务的通用解决方案

## 示例

引入pom依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-tool-knife4j-starter</artifactId>
    <version>${lyloou.component.version}</version>
</dependency>
```

添加配置

```yaml
component:
  swagger:
    enable: true
    items:
      default:
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
      redismanager:
        # 是否启用
        enable: true
        group-name: redismanager
        # 扫描的包，多个包使用逗号隔开
        base-package: com.lyloou.component.redismanager
        contact-name: lyloou
        contact-email: lyloou@qq.com
        contact-url: http://lyloou.com
        description: swagger api
        title: redismanager API Documents
        url: http://lyloou.com
        version: 0.0.1
knife4j:
  enable: ${component.swagger.enable}
  basic:
    enable: true
    username: admin
    password: admin
```