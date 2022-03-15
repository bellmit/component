package com.lyloou.component.file.ali;

import lombok.Data;

/**
 * @author lilou
 * @date 2022/3/15 10:33
 */
@Data
public class AliFileItemModel {
    private String url;
    private Boolean success;
    private String failMsg;
    /**
     * 耗时
     */
    private Long intervalMs;
}
