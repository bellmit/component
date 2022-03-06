package com.lyloou.component.tool.execcommand;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lilou
 * @since 2021/8/17
 */
@SpringBootApplication
@RestController
public class ExecCommandApplication {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        SpringApplication.run(ExecCommandApplication.class, args);
    }

    @GetMapping("/task1")
    public String t1() {
        count.set(0);
        final ThreadPoolTaskExecutor executor = taskExecutor();
        for (int i = 0; i < 1000; i++) {
            executor.submit(this::printTask);
        }
        return "ok";
    }

    @GetMapping("/task2")
    public String t2() {
        count.set(1);
        final ThreadPoolTaskExecutor executor = taskExecutor2();
        for (int i = 0; i < 1000; i++) {
            executor.submit(this::printTask);
        }
        return "ok";
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors() * 64);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(ThreadUtil.newNamedThreadFactory("taskExecutor~", false));
        executor.initialize();
        return executor;
    }


    @Bean
    public ThreadPoolTaskExecutor taskExecutor2() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(60);
        executor.initialize();
        return executor;
    }

    private final AtomicLong count = new AtomicLong(0);

    private void printTask() {
        final Snowflake snowflake = IdUtil.getSnowflake(0, 0);
        final long taskId = snowflake.nextId();

        System.out.println(Thread.currentThread().getName() + ":" + count.getAndAdd(1) + "~" + taskId);
    }
}
