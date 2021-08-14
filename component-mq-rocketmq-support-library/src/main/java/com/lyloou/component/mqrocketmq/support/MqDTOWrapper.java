package com.lyloou.component.mqrocketmq.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息体封装类
 *
 * @author: ma wei long
 * @date: 2020年6月27日 上午11:59:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqDTOWrapper<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1762409052644257813L;

    /**
     * 消息体数据
     */
    private T data;

    /**
     * 消息id，可以用来避免重复消费
     */
    private String messageId;
}
