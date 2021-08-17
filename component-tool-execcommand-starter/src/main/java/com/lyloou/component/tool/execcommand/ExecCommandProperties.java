package com.lyloou.component.tool.execcommand;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lilou
 * @since 2021/8/17
 */
@Data
@ConfigurationProperties("component.execcommand")
public class ExecCommandProperties {
    /**
     * 日志存储路径
     */
    private String logFileDir;
    /**
     * 是否开启日志文件记录
     */
    private boolean enableLogFile = false;

    /**
     * taskId生成器，（雪花算法 workerId）
     */
    private long workerId = 0L;
    /**
     * taskId生成器，（雪花算法 datacenterId）
     */
    private long datacenterId = 0L;
}
