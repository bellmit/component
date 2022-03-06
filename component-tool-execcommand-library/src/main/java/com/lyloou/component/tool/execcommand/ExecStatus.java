package com.lyloou.component.tool.execcommand;

/**
 * 参考线程的状态：
 * [Java线程的6种状态及切换(透彻讲解)_潘建南的博客-CSDN博客_线程状态](https://blog.csdn.net/pange1991/article/details/53860651)
 *
 * @author lilou
 * @since 2021/8/16
 */
public interface ExecStatus {

    /**
     * 0：未运行
     */
    int NEW = 0;

    /**
     * 1：正准备运行
     */
    int RUNNABLE = 1;

    /**
     * 2. 正常运行中
     */
    int RUNNING = 2;

    /**
     * 3. 错误运行中
     */
    int RUNNING_ERROR = 3;

    /**
     * 4：已经结束
     */
    int TERMINATED = 4;
}

