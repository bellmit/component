## 上传文件，七牛api方式

1. 加依赖

```xml

<dependency>
    <groupId>com.lyloou</groupId>
    <artifactId>component-file-qiniu-controller-starter</artifactId>
    <version>${lyloou.component.version}</version>
</dependency>
```

2. 添加redis配置

```yml
file:
  qiniu:
    accessKey: "xxxxxxxxxxxxxxxxxx"
    secretKey: "xxxxxxxxxxxxxxxxx"
    bucket: "lyloou"
    bucketUrl: "http://cdn.example.com"
    uploadDir: "tmp/"
```

3. 调用方式

```
POST  http://localhost:8080/file/qiniu/upload
form-data
key: file
value: filepath（可以上传多个文件）
```

![image-20210429111534361](http://cdn.lyloou.com/img/20210429111534.png)

## 七牛文档

https://developer.qiniu.com/kodo/1239/java#upload-bytes
