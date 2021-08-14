package com.lyloou.component.mqrocketmq.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: ma wei long
 * @date: 2020年6月27日 下午2:27:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelPayOrderDTO implements Serializable {

    private static final long serialVersionUID = -191417001399470940L;

    private Long orderId;
}
