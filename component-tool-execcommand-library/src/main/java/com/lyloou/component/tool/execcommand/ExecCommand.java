package com.lyloou.component.tool.execcommand;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.listener.ProcessListener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * https://www.codeleading.com/article/34874374574/
 * 执行命令行或脚本
 */
@Slf4j
public class ExecCommand extends Observable implements Runnable {

    private final ExecTask task;

    private final List<OutputStream> outputStreamList = new ArrayList<>();
    private final List<OutputStream> errorOutputStreamList = new ArrayList<>();

    public ExecCommand(ExecTask task) {
        this.task = task;
        task.setTaskProgress(0);
        task.setProcessStatus(ExecStatus.NEW);
    }

    public void execute() throws InterruptedException, TimeoutException, IOException {

        // 获取和验证参数
        Assert.notNull(task.getCommand(), "command can not null");
        List<String> commandList = new ArrayList<>();
        commandList.add(task.getCommand());
        if (Objects.nonNull(task.getParam())) {
            commandList.addAll(Arrays.asList(task.getParam()));
        }

        // 获取执行器
        final ProcessExecutor executor = new ProcessExecutor().command(commandList);


        // 添加输出流
        if (CollUtil.isNotEmpty(outputStreamList)) {
            for (OutputStream outputStream : outputStreamList) {
                executor.redirectOutputAlsoTo(outputStream);
            }
        }
        if (CollUtil.isNotEmpty(errorOutputStreamList)) {
            for (OutputStream outputStream : errorOutputStreamList) {
                executor.redirectErrorAlsoTo(outputStream);
            }
        }

        executor.destroyOnExit()
                .addListener(getProcessListener())
                .execute();

    }

    @SneakyThrows(IOException.class)
    public void printTask() {
        if (outputStreamList.isEmpty()) {
            return;
        }

        final String s = task.toString() + "\n";
        final byte[] taskContent = s.getBytes();
        for (OutputStream outputStream : outputStreamList) {
            outputStream.write(taskContent);
            outputStream.flush();
        }
    }

    private ProcessListener getProcessListener() {
        return new ProcessListener() {
            @Override
            public void beforeStart(ProcessExecutor executor) {
                task.setTaskProgress(0);
                task.setProcessStatus(ExecStatus.NEW);
                task.setProcessLine(null);
                notifyTaskChanged();
                printTask();
            }

            @Override
            public void afterStart(Process process, ProcessExecutor executor) {
                task.setTaskProgress(5);
                task.setProcessStatus(ExecStatus.RUNNABLE);
                task.setProcessLine(null);
                notifyTaskChanged();
            }

            @Override
            public void afterFinish(Process process, ProcessResult result) {
                task.setTaskProgress(99);
                task.setProcessStatus(ExecStatus.RUNNING);
                task.setProcessLine(null);
                task.setExitCode(result.getExitValue());
                notifyTaskChanged();
            }

            @Override
            public void afterStop(Process process) {
                task.setTaskProgress(100);
                task.setProcessStatus(ExecStatus.TERMINATED);
                task.setProcessLine(null);
                task.setExitCode(process.exitValue());
                notifyTaskChanged();
                printTask();
            }
        };
    }

    public void notifyTaskChanged() {
        task.addRandomProgress();
        setChanged();
        notifyObservers(task);
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (Exception e) {
            throw new ExecException(e.getMessage(), e);
        }
    }

    public void clearOutputStreamList() {
        outputStreamList.clear();
        errorOutputStreamList.clear();
    }

    public void addOutputStream(OutputStream outputStream) {
        if (Objects.nonNull(outputStream)) {
            outputStreamList.add(outputStream);
        }
    }

    public void addErrorOutputStream(OutputStream outputStream) {
        if (Objects.nonNull(outputStream)) {
            errorOutputStreamList.add(outputStream);
        }
    }
}
