package com.lyloou.component.redismanager;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lilou
 * @since 2021/4/6
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {
    private Integer id;
    private Integer age;
    private String name;
    private String sex;
}
