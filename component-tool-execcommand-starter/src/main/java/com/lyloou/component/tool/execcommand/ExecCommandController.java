package com.lyloou.component.tool.execcommand;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.lyloou.component.dto.ConvertUtils;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.exceptionhandler.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

    @Value("${server.port}")
    private int port;

    @Autowired
    private ExecCommandProperties execCommandProperties;

    @Autowired
    private ThreadPoolTaskExecutor execCommandExecutor;

    @Autowired
    private Snowflake idGen;


    @SneakyThrows(UnknownHostException.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "command", value = "命令"),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "param", value = "命令参数")
    })
    @ApiOperation(value = "执行命令行脚本文件", notes = "", httpMethod = "GET")
    @GetMapping("exec")
    public SingleResponse<ExecTaskModel> exec(String command, String[] param) {
        final String taskId = idGen.nextIdStr();

        final ExecTask task = new ExecTask(taskId, command, param);

        final ExecCommand execCommand = new ExecCommand(task);

        final String logName = "TASK-" + task.getTaskId();
        execCommand.addOutputStream(ExecStreamTool.getSlf4jStream(logName).asInfo());
        execCommand.addErrorOutputStream(ExecStreamTool.getSlf4jStream(logName).asError());

        if (execCommandProperties.isEnableLogFile()) {
            execCommand.addOutputStream(ExecStreamTool.getFileOutputStream(getLogFilePath(taskId)));
        }

        execCommandExecutor.submit(execCommand);
        final ExecTaskModel taskModel = ConvertUtils.convert(task, ExecTaskModel.class);
        String ip = InetAddress.getLocalHost().getHostAddress();

        taskModel.setLogUrl(StrUtil.format("http://{}:{}/command/log?taskId={}", ip, port, taskId));
        return SingleResponse.buildSuccess(taskModel);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "taskId", value = "任务id")
    })
    @ApiOperation(value = "获取执行任务", notes = "", httpMethod = "GET")
    @GetMapping("log")
    public String getLog(String taskId) {
        AssertUtil.notNullParam(taskId, "无效的taskID：" + taskId);

        final List<String> dataList = new ArrayList<>();
        IoUtil.readLines(FileUtil.getInputStream(getLogFilePath(taskId)), Charset.forName("GBK"), dataList);
        return String.join("\r\n", dataList);
    }

    private String getLogFilePath(String taskId) {
        final String logFileDir = execCommandProperties.getLogFileDir();
        final String today = DateUtil.formatDate(new Date());
        String logFilePath = logFileDir + today + "/" + taskId + ".log";
        return logFilePath;
    }
}
