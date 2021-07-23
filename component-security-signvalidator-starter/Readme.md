## 签名目的

- 防篡改
- 防抓包
- 防刷
- 更安全

## 签名的主要防御措施：

一、 验证签名

> 将有效的参数按字典排序，并 md5 或 sha 加密（可以自定义加密算法）得到 param_sign，服务端依据同样的算法得到 server_sign，对比传递过来的 param_sign，不相等断定签名无效。
> 如果请求是 RequestBody ，当作 key 为 "body" ，value 为 json 字符串值传入校验。

二、 请求的时间是否在限制的时间内（如：1 分钟内）

> 判断请求的时间戳是否在允许的范围内

三、 请求方是否被接口服务注册登记

> 判断 clientId 是否有效

四、 是否重复请求

> 通过缓存验证 key (由 clientId+nonce 组成)是否已经存在，nonce 是随机生成的字符串
> 默认开启此功能，并由本地来缓存，可以自定义缓存实现微服务的场景（如redis 等等）。

## WIKI文档

- [1.1服务端基本使用](./doc/1.1服务端基本使用.md)
- [1.2客户端基本使用](./doc/1.2客户端基本使用.md)
- [2.配置说明](./doc/2.配置说明.md)
- [3.自定义缓存](./doc/3.自定义缓存.md)
- [4.自定义签名](doc/4.自定义验证器.md)
- [5.签名异常捕获](./doc/5.签名异常捕获.md)

## 实现原理

实现机制：通过拦截器，在真正 handler 之前，对请求拦截后统一处理。

我们可以继承 `HandlerInterceptorAdapter` 类，然后注册给 WebMvcConfigure，这样我们的拦截器就可以生效了

```java
public class SignAutoConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignInterceptor());
    }
}
```

签名的原理是：

- 客户端将所有有效参数，将key按字典排序，加上密钥，然后md5加密得到sign
- 服务端用同样的方式得到 sign，比较参数中的 sign 和自己生成的 sign

## 特性

- 支持上面提到的所有防御措施
- 支持多客户端
- 支持自定义缓存
- 支持自定义验证器
- 支持全局和局部的开关控制

## 客户端工具

**Java客户端工具**

> SimpleHttpUtil
> 具体见 [1.2客户端基本使用](./doc/1.2客户端基本使用.md)

**Js客户端工具**

## 参考资料

- [再谈前后端 API 签名安全？](https://mp.weixin.qq.com/s/UzEsLtv_ald6Yw1XVAhmwg)
- [前后端 API 交互如何保证数据安全性？](https://mp.weixin.qq.com/s/ChwFubuX0HcB0DgIEi5Tog)
- [使用 nonce 巩固接口签名安全\_猿天地的技术博客\_51CTO 博客](https://blog.51cto.com/u_14888386/2515879)
- [【AOP 实践】接口签名校验*Jayin*的博客-CSDN 博客](https://blog.csdn.net/qq_36641443/article/details/108475613)
- [基于 Spring Boot 以及 Redis 使用 Aop 来实现 Api 接口签名验证 - 知乎](https://zhuanlan.zhihu.com/p/112555362)

## 示例源码

https://github.com/lyloou/component-parent-test/tree/master/component-security-signvalidator-starter-test