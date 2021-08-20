package com.lyloou.component.cache.datacache;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 缓存配置类
 * </p>
 *
 * @author lilou
 */
@Getter
@Setter
public class DataCacheProperties {

    /**
     * 默认缓存过期时间：3分钟，单位秒
     */
    private long timeout = 3 * 60;

    /**
     * 是否开启定时清理功能{@link DataDefaultCache#pruneCache()}
     */
    private boolean schedulePrune = true;

}
