package com.lyloou.component.tool.execcommand;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExecTask {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 命令
     */
    private String command;

    /**
     * 命令参数
     */
    private String[] param;

    /**
     * 任务执行完成后的退出状态码
     */
    private Integer exitCode;

    /**
     * 任务进度
     */
    private Integer taskProgress;


    /**
     * 执行状态
     *
     * @see ExecStatus
     */
    private Integer processStatus;

    /**
     * 当前执行行日志
     */
    private String processLine;

    /**
     * 额外信息
     */
    private String extValues;


    public ExecTask(String taskId, String command, String... param) {
        this.taskId = taskId;
        this.command = command;
        this.param = param;
    }

    public void addRandomProgress() {
        if (taskProgress == null) {
            taskProgress = 0;
            return;
        }

        if (taskProgress == 100) {
            return;
        }

        taskProgress++;

        if (taskProgress >= 99) {
            taskProgress = 99;
        }
    }

}