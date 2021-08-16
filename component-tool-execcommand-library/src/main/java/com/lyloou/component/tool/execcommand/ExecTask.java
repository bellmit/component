package com.lyloou.component.tool.execcommand;

import lombok.Data;

import java.util.List;

@Data
class ExecTask {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 命令列表
     */
    private List<String> command;

    /**
     * 是否开启日志文件记录功能
     */
    private boolean enableLogFile = false;

    /**
     * 日志文件路径
     */
    private String logFilePath;

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
     * 0：未运行
     * 1：正在运行
     * 2：已经结束
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

    public ExecTask(String taskId, List<String> command) {
        this.taskId = taskId;
        this.command = command;
    }
}