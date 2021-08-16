package com.lyloou.component.tool.execcommand;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * https://www.codeleading.com/article/34874374574/
 * 执行命令行或脚本
 */
@Slf4j
public class ExecCommandExample {


    public static void main(String[] args) {
        final Snowflake snowflake = IdUtil.getSnowflake(0, 0);
        final long taskId = snowflake.nextId();

        final ExecTask task = new ExecTask(String.valueOf(taskId), Arrays.asList("ping", "lyloou.com"));
        task.setEnableLogFile(true);
        task.setLogFilePath("D:/" + taskId + ".txt");
        final ExecCommand execCommand = new ExecCommand(task);

        // 添加监听器
        execCommand.addObserver((o, arg) -> {
            ExecTask t = (ExecTask) arg;
            System.out.println(t);
        });

        new Thread(execCommand).start();
    }

}
