package com.lyloou.component.tool.execcommand;

/**
 * @author lilou
 * @since 2021/8/16
 */
public interface ProcessStatus {

    /**
     * 0：未运行
     */
    int PENDING = 0;

    /**
     * 1：正在运行
     */
    int RUNNING = 1;

    /**
     * 2：已经结束
     */
    int FINISHED = 2;
}

