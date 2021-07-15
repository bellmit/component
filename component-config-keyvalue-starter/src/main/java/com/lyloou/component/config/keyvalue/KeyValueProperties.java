package com.lyloou.component.config.keyvalue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lilou
 * @since 2021/7/15
 */
@ConfigurationProperties(prefix = "key-value")
@Data
public class KeyValueProperties {
    /**
     * 键值对的类型，一个类型可以包括多个键值对
     */
    Map<String, List<KeyValue>> type = new LinkedHashMap<>();
    /**
     * 搜索时是否忽略大小写
     */
    Boolean ignoreCase = true;
}
