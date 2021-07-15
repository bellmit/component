## 简单的配置管理，基于键值对

结合 nacos ，可以实现动态刷新

## 示例：

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-config-keyvalue-starter</artifactId>
</dependency>
```

```yaml
key-value:
  type:
    QQ:
      - key: home
        value: http://qq.com
      - key: weixin
        value: http://weixin.com
    WEIBO:
      - key: home
        value: http://weibo.com
      - key: blog
        value: http://blog.weibo.com
  #默认为true
  ignore-case: true 
```

```shell
curl -X GET "http://localhost:8080/keyValue/getAllKeyValue" -H  "accept: */*"
curl -X GET "http://localhost:8080/keyValue/getKeyValueByType?type=qq" -H  "accept: */*"
curl -X GET "http://localhost:8080/keyValue/getValueByTypeAndKey?key=home&type=WEIBO" -H  "accept: */*"
```