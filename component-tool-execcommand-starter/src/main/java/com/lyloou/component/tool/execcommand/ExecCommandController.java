package com.lyloou.component.tool.execcommand;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lilou
 * @since 2021/8/17
 */
@Api(value = "/command", tags = {"命令接口"})
@RequestMapping("/command")
@RestController
public class ExecCommandController {

    @Autowired
    private ExecCommandProperties execCommandProperties;

    @Autowired
    private ThreadPoolTaskExecutor execCommandExecutor;

    @Autowired
    private Snowflake idGen;


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "command", value = "命令"),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "param", value = "命令参数")
    })
    @ApiOperation(value = "执行命令行脚本文件", notes = "", httpMethod = "GET")
    @GetMapping("exec")
    public SingleResponse<ExecTask> exec(String command, String[] param) {
        final String taskId = idGen.nextIdStr();

        final ExecTask task = new ExecTask(taskId, command, param);

        final ExecCommand execCommand = new ExecCommand(task);

        // final String logName = "TASK-" + task.getTaskId();
        // execCommand.addOutputStream(ExecStreamTool.getSlf4jStream(logName).asInfo());
        // execCommand.addErrorOutputStream(ExecStreamTool.getSlf4jStream(logName).asError());

        if (execCommandProperties.isEnableLogFile()) {
            execCommand.addOutputStream(ExecStreamTool.getFileOutputStream(getLogFilePath(taskId)));
        }

        // 添加监听器
        execCommand.addOutputStream(new LogOutputGbkStream(task, execCommand));
        execCommand.addObserver((o, arg) -> {
            ExecTask t = (ExecTask) arg;
            System.out.println(t);
        });

        execCommandExecutor.submit(execCommand);
        return SingleResponse.buildSuccess(task);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "taskId", value = "任务id")
    })
    @ApiOperation(value = "获取执行任务", notes = "", httpMethod = "GET")
    @GetMapping("log")
    public String getLog(String taskId) {
        AssertUtil.notNullParam(taskId, "无效的taskID：" + taskId);

        final List<String> dataList = new ArrayList<>();
        IoUtil.readLines(FileUtil.getInputStream(getLogFilePath(taskId)), Charset.defaultCharset(), dataList);
        return String.join("\n", dataList);
    }

    private String getLogFilePath(String taskId) {
        final String logFileDir = execCommandProperties.getLogFileDir();
        final String today = DateUtil.formatDate(new Date());
        String logFilePath = logFileDir + today + "/" + "taskId" + ".log";
        return logFilePath;
    }
}
