package com.lyloou.component.mqcommon.support.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author lilou
 * @since 2021/8/11
 */
@Data
@TableName(value = "mq_exception")
public class MqExceptionDO {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "topic")
    private String topic;

    @TableField(value = "message")
    private String message;

    @TableField(value = "`exception`")
    private String exception;

    @TableField(value = "create_time")
    private Date createTime;
}