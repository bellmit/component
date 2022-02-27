package com.lyloou.component.tool.execcommand;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lyloou.component.dto.SingleResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lilou
 * @date 2021/11/17 16:22
 */
@RestController
@Api(tags = "工具api")
@RequestMapping("shell")
@Slf4j
public class ShellController {

    @Value("${log.path:/log/shell}")
    private String baseLogDir;

    @Autowired
    ThreadPoolTaskExecutor execCommandExecutor;

    @GetMapping("/features")
    public SingleResponse<Map<String, String>> features() {
        return SingleResponse.buildSuccess(ShellEnum.features());
    }

    @GetMapping("/log")
    public String log(String name, String taskId) {
        return FileUtil.readString(shellLogPath(name, taskId), StandardCharsets.UTF_8);
    }

    private String shellLogPath(String name, String taskId) {
        return StrUtil.format("{}/shell/{}/{}.log", baseLogDir, name, taskId);
    }

    @GetMapping("/call")
    public void call(String name, HttpServletResponse response) throws IOException, InterruptedException {
        String taskId = IdUtil.getSnowflakeNextIdStr();
        ShellEnum shell = ShellEnum.getShell(name);
        final ExecTask task = new ExecTask(taskId, Arrays.asList("ping", "ip138.com"));
        task.setEnableLogFile(true);
        task.setLogFilePath(shellLogPath(name, taskId));
        final ExecCommand execCommand = new ExecCommand(task);

        // 添加监听器
        execCommand.addObserver((o, arg) -> {
            ExecTask t = (ExecTask) arg;
            log.info("print log:{}", t);
        });
        execCommandExecutor.execute(execCommand);
        TimeUnit.SECONDS.sleep(10);

        // 重定向到日志页面
        response.sendRedirect(StrUtil.format("./log?name={}&taskId={}", name, taskId));
    }
}
