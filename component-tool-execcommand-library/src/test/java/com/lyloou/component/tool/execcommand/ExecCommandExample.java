package com.lyloou.component.tool.execcommand;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * https://www.codeleading.com/article/34874374574/
 * 执行命令行或脚本
 */
@Slf4j
public class ExecCommandExample {


    public static void main(String[] args) {
        a();
        // b();
    }

    private static void b() {
        try {
            // 执行ping命令
            Process process = Runtime.getRuntime().exec("ping lyloou.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));


            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void a() {
        final Snowflake snowflake = IdUtil.getSnowflake(0, 0);
        final long taskId = snowflake.nextId();

        // final ExecTask task = new ExecTask(String.valueOf(taskId), "ping", "-n", "2", "lyloou.com");
        final ExecTask task = new ExecTask(String.valueOf(taskId), "java", "-version");

        final ExecCommand execCommand = new ExecCommand(task);

        // final String logName = "TASK-" + task.getTaskId();
        // execCommand.addOutputStream(ExecStreamTool.getSlf4jStream(logName).asInfo());
        // execCommand.addErrorOutputStream(ExecStreamTool.getSlf4jStream(logName).asError());

        final LogOutputGbkStream gbkStream = new LogOutputGbkStream(task, execCommand);
        execCommand.addOutputStream(gbkStream);


        final String logFileDir = "D:/logs/exec-task/";
        final String today = DateUtil.formatDate(new Date());
        String logFilePath = logFileDir + today + "/" + "taskId" + ".log";
        execCommand.addOutputStream(ExecStreamTool.getFileOutputStream(logFilePath));


        // 添加监听器
        execCommand.addObserver((o, arg) -> {
            ExecTask t = (ExecTask) arg;
            System.out.println(t.getProcessLine());
        });

        new Thread(execCommand).start();
    }

}
