package com.lyloou.component.redismanager;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lilou
 * @since 2021/4/8
 */
@Data
public class CacheConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cacheName;
    private Boolean cacheNullValues;
    private Integer ttl;
}
