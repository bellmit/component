package com.lyloou.component.tool.execcommand;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Observable;
import java.util.concurrent.TimeoutException;

/**
 * https://www.codeleading.com/article/34874374574/
 * 执行命令行或脚本
 */
@Slf4j
public class ExecCommand extends Observable implements Runnable {

    private final ExecTask task;

    public ExecCommand(ExecTask task) {
        this.task = task;
        task.setTaskProgress(0);
        task.setProcessStatus(ProcessStatus.PENDING);
    }

    public void execute() throws InterruptedException, TimeoutException, IOException {
        ExecTask task = this.task;
        task.setTaskProgress(5);
        task.setProcessStatus(ProcessStatus.RUNNING);
        notifyTaskChanged();

        final OutputStreamWriter writer = getWriter(task);

        final ProcessExecutor executor = new ProcessExecutor().command(task.getCommand());
        final ProcessResult execute = executor
                .destroyOnExit()
                .redirectOutput(new LogOutputStream() {
                    @Override
                    protected void processLine(String s) {
                        task.setProcessStatus(ProcessStatus.RUNNING);
                        task.setProcessLine(s);

                        if (writer != null) {
                            outputData(s, writer);
                        }

                        notifyTaskChanged();
                    }
                })
                .redirectError(new LogOutputStream() {
                    @Override
                    protected void processLine(String s) {
                        task.setProcessStatus(ProcessStatus.RUNNING);
                        task.setProcessLine(s);

                        if (writer != null) {
                            outputData(s, writer);
                        }

                        notifyTaskChanged();
                    }
                })
                .execute();

        IoUtil.close(writer);

        task.setTaskProgress(100);
        task.setProcessStatus(ProcessStatus.FINISHED);
        task.setProcessLine(null);
        task.setExitCode(execute.getExitValue());
        notifyTaskChanged();
    }

    private void notifyTaskChanged() {
        setChanged();
        notifyObservers(task);
    }

    private static OutputStreamWriter getWriter(ExecTask task) {
        if (!task.isEnableLogFile()) {
            return null;
        }

        if (StrUtil.isEmpty(task.getLogFilePath())) {
            throw new IllegalArgumentException("开启了日志文件记录功能，但是没有传入路径");
        }

        try {
            return IoUtil.getWriter(FileUtil.getOutputStream(task.getLogFilePath()), Charset.defaultCharset());
        } catch (Exception e) {
            throw new IllegalArgumentException("开启了日志文件记录功能，但是操作文件失败，" + e.getMessage());
        }
    }

    private static void outputData(String s, OutputStreamWriter writer) {
        try {
            writer.append(s)
                    .append("\n")
                    .flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (InterruptedException | TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
