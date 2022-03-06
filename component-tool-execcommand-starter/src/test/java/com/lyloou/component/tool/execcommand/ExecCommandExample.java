package com.lyloou.component.tool.execcommand;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;

@Slf4j
public class ExecCommandExample {


    public static void main(String[] args) throws Exception {
        final ProcessExecutor executor = new ProcessExecutor().command("bash.exe", "D:/aa.sh");
        final ProcessResult execute = executor
                .destroyOnExit()
                .redirectOutput(new LogOutputStream() {
                    @Override
                    protected void processLine(String s) {
                        System.out.println(s);
                    }
                })
                .redirectError(new LogOutputStream() {
                    @Override
                    protected void processLine(String s) {
                        System.out.println(s);
                    }
                })
                .execute();

        // a();
    }

    private static void a() {
        final Snowflake snowflake = IdUtil.getSnowflake(0, 0);
        final long taskId = snowflake.nextId();

        // final ExecTask task = new ExecTask(String.valueOf(taskId), Arrays.asList("sh", "D:\\w\\box\\bin\\commit.sh"));
        final ExecTask task = new ExecTask(String.valueOf(taskId), "sh", "D:\\aa.sh");
        final ExecCommand execCommand = new ExecCommand(task);

        // 添加监听器
        execCommand.addObserver((o, arg) -> {
            ExecTask t = (ExecTask) arg;
            System.out.println(t);
        });

        new Thread(execCommand).start();
        System.out.println(SystemUtil.getHostInfo());
        System.out.println(SystemUtil.getUserInfo());
    }

}
