package com.lyloou.component.cache.datacache;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存清理调度器
 *
 * @author lilou
 * @since 2021/7/13
 */
public enum DataCacheScheduler {
    /**
     * 单例
     */
    INSTANCE;

    private final AtomicInteger cacheTaskNumber = new AtomicInteger(1);
    private ScheduledExecutorService scheduler;

    DataCacheScheduler() {
        create();
    }

    private void create() {
        this.shutdown();
        this.scheduler = new ScheduledThreadPoolExecutor(10,
                r -> new Thread(r, String.format("Data-Task-%s", cacheTaskNumber.getAndIncrement())));
    }

    private void shutdown() {
        if (null != scheduler) {
            scheduler.shutdown();
        }
    }

    public void schedule(Runnable task, long delay) {
        this.scheduler.scheduleAtFixedRate(task, delay, delay, TimeUnit.SECONDS);
    }
}
