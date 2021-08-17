package com.lyloou.component.tool.execcommand;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import com.lyloou.component.cache.datacache.DataCache;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author lilou
 * @since 2021/8/17
 */
@Api(tags = "【component】命令脚本管理接口")
@RequestMapping("/execcommand")
@RestController
public class ExecCommandController {

    @Autowired
    ExecCommandProperties execCommandProperties;

    @Autowired
    @Qualifier("execCommandExecutor")
    Executor execCommandExecutor;

    @Autowired
    @Qualifier("execCommandSnowflake")
    Snowflake execCommandSnowflake;

    @Autowired
    DataCache dataCache;

    @GetMapping("execCommandFile")
    @ApiOperation(value = "执行命令行脚本文件")
    public SingleResponse<ExecTask> execCommandFile(@ApiParam("脚本文件名称") String filename) {
        final String taskId = execCommandSnowflake.nextIdStr();
        final List<String> command;
        if (SystemUtil.getOsInfo().isWindows()) {
            command = Arrays.asList("cmd", "/c", filename);
        } else {
            command = Arrays.asList("/bin/sh", "-c", filename);
        }
        final ExecTask task = new ExecTask(taskId, command);
        final boolean enableLogFile = execCommandProperties.isEnableLogFile();
        task.setEnableLogFile(enableLogFile);
        task.setLogFilePath(getLogFilePath(enableLogFile, taskId));

        final ExecCommand execCommand = new ExecCommand(task);
        // 添加监听器
        execCommand.addObserver((o, arg) -> {
            ExecTask t = (ExecTask) arg;
            cacheTask(taskId + "", t);
        });

        cacheTask(taskId, task);
        execCommandExecutor.execute(execCommand);
        return SingleResponse.buildSuccess(task);
    }

    @GetMapping("execCommand")
    @ApiOperation(value = "执行命令")
    public SingleResponse<ExecTask> execCommand(@ApiParam("命令列表") String[] command) {
        AssertUtil.notNullParam(command, "无效的命令");

        final String taskId = execCommandSnowflake.nextIdStr();
        final ExecTask task = new ExecTask(taskId, Arrays.asList(command));
        final boolean enableLogFile = execCommandProperties.isEnableLogFile();
        task.setEnableLogFile(enableLogFile);
        task.setLogFilePath(getLogFilePath(enableLogFile, taskId));

        final ExecCommand execCommand = new ExecCommand(task);
        // 添加监听器
        execCommand.addObserver((o, arg) -> {
            ExecTask t = (ExecTask) arg;
            cacheTask(taskId, t);
        });

        cacheTask(taskId, task);
        execCommandExecutor.execute(execCommand);
        return SingleResponse.buildSuccess(task);
    }

    private void cacheTask(String taskId, ExecTask task) {
        final String taskStr = JSONUtil.toJsonStr(task);
        dataCache.set(taskId, taskStr);
    }

    private String getLogFilePath(boolean enableLogFile, String taskId) {
        if (!enableLogFile) {
            return null;
        }
        return execCommandProperties.getLogFileDir() + taskId + ".txt";
    }

    @GetMapping("getExecTask")
    @ApiOperation(value = "获取执行任务")
    public SingleResponse<ExecTask> getExecTask(@ApiParam("任务id") String taskId) {
        AssertUtil.notNullParam(taskId, "无效的taskID：" + taskId);

        final String taskStr = dataCache.get(taskId);
        AssertUtil.notNullParam(taskStr, "找不到taskId为" + taskId + "的执行任务");

        return SingleResponse.buildSuccess(JSONUtil.toBean(taskStr, ExecTask.class));
    }

    @GetMapping("getLogFile")
    @ApiOperation(value = "获取执行任务")
    public String getLogFile(@ApiParam("任务id") String taskId) throws IOException {
        AssertUtil.notNullParam(taskId, "无效的taskID：" + taskId);
        final String taskStr = dataCache.get(taskId);
        AssertUtil.notNullParam(taskStr, "找不到taskId为" + taskId + "的执行任务");

        final ExecTask execTask = JSONUtil.toBean(taskStr, ExecTask.class);
        final String logFilePath = execTask.getLogFilePath();
        final List<String> dataList = new ArrayList<>();
        IoUtil.readLines(FileUtil.getUtf8Reader(logFilePath), dataList);
        return String.join("\n", dataList);
    }
}
