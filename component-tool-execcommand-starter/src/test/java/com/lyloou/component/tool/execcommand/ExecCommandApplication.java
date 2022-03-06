package com.lyloou.component.tool.execcommand;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

/**
 * @author lilou
 * @since 2021/8/17
 */
@SpringBootApplication
@RestController
@Slf4j
public class ExecCommandApplication implements ApplicationListener<WebServerInitializedEvent> {


    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        SpringApplication.run(ExecCommandApplication.class, args);
    }

    /**
     * [Springboot中获取本机IP、端口号和Context-path，项目启动后输出路径_一碗单炒饭的专栏-CSDN博客_springboot 获取本机ip](https://blog.csdn.net/djzhao627/article/details/105674827)
     */
    @SneakyThrows
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        WebServer server = event.getWebServer();
        WebServerApplicationContext context = event.getApplicationContext();
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        int port = server.getPort();
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        String baseUrl = StrUtil.format("http://{}:{}{}", ip, port, contextPath);

        log.info("\n---------------------------------------------------------\n" +
                "\tApplication is running! Access address:\n" +
                "\tLocal:\t\thttp://localhost:{}" +
                "\n\tExternal:\t{}" +
                "\n---------------------------------------------------------\n", port, baseUrl);
    }

}
