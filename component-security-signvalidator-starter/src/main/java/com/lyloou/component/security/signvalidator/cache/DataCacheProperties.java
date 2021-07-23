package com.lyloou.component.security.signvalidator.cache;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 缓存配置类
 * </p>
 */
@Getter
@Setter
public class DataCacheProperties {

    /**
     * 默认缓存过期时间：3分钟，单位秒
     */
    private long timeout = 3 * 60;

    /**
     * 是否开启定时{@link DataDefaultCache#pruneCache()}的任务
     */
    private boolean schedulePrune = true;

    /**
     * 缓存前缀 默认 COMPONENT::SIGNED::
     */
    private String prefix = "COMPONENT::SIGNED::";

}
