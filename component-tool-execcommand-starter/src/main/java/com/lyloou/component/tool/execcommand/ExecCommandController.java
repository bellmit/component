package com.lyloou.component.tool.execcommand;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.lyloou.component.cache.datacache.DataCache;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author lilou
 * @since 2021/8/17
 */
@Api(value = "/execcommand", tags = {""})
@RequestMapping("/execcommand")
@RestController
public class ExecCommandController {

    @Autowired
    private ExecCommandProperties execCommandProperties;

    @Autowired
    private ThreadPoolTaskExecutor execCommandExecutor;

    @Autowired
    private Snowflake execCommandSnowflake;

    @Autowired
    DataCache dataCache;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "filename", value = "文件名")
    })
    @ApiOperation(value = "执行命令行脚本文件", notes = "", httpMethod = "GET")
    @GetMapping("execCommandFile")
    public SingleResponse<ExecTask> execCommandFile(String filename) {
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
            System.out.println(t);
            System.out.println(Thread.currentThread().getName());
            cacheTask(taskId + "", t);
        });

        cacheTask(taskId, task);
        execCommandExecutor.submit(execCommand);
        return SingleResponse.buildSuccess(task);
    }


    private void cacheTask(String taskId, ExecTask task) {
        final String taskStr = JSON.toJSONString(task);
        dataCache.set(taskId, taskStr);
    }

    private String getLogFilePath(boolean enableLogFile, String taskId) {
        if (!enableLogFile) {
            return null;
        }
        return execCommandProperties.getLogFileDir() + taskId + ".txt";
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "taskId", value = "任务id")
    })
    @ApiOperation(value = "获取执行任务", notes = "", httpMethod = "GET")
    @GetMapping("getExecTask")
    public SingleResponse<ExecTask> getExecTask(String taskId) {
        AssertUtil.notNullParam(taskId, "无效的taskID：" + taskId);

        final String taskStr = dataCache.get(taskId);
        AssertUtil.notNullParam(taskStr, "找不到taskId为" + taskId + "的执行任务");

        return SingleResponse.buildSuccess(JSON.parseObject(taskStr, ExecTask.class));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "taskId", value = "任务id")
    })
    @ApiOperation(value = "获取执行任务", notes = "", httpMethod = "GET")
    @GetMapping("getLogFile")
    public String getLogFile(String taskId) {
        AssertUtil.notNullParam(taskId, "无效的taskID：" + taskId);
        final String taskStr = dataCache.get(taskId);
        AssertUtil.notNullParam(taskStr, "找不到taskId为" + taskId + "的执行任务");

        final ExecTask execTask = JSON.parseObject(taskStr, ExecTask.class);
        final String logFilePath = execTask.getLogFilePath();
        final List<String> dataList = new ArrayList<>();
        IoUtil.readLines(FileUtil.getUtf8Reader(logFilePath), dataList);
        return String.join("\n", dataList);
    }
}
