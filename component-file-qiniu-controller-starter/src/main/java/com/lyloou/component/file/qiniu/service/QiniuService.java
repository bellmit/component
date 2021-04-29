package com.lyloou.component.file.qiniu.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author lilou
 * @since 2021/4/29
 */
public interface QiniuService {
    /**
     * 上传文件
     *
     * @param files 文件
     * @return 结果：以 file->url的方式组成的map
     */
    Map<String, String> uploadFile(@RequestParam("file") MultipartFile[] files);
}
