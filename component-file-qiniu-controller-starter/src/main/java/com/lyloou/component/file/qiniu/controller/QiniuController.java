package com.lyloou.component.file.qiniu.controller;

import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.file.qiniu.service.QiniuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author lilou
 * @since 2021/4/29
 */
@RestController
@RequestMapping("/file/qiniu")
public class QiniuController {

    @Autowired
    QiniuService qiniuService;


    @PostMapping("/upload")
    public SingleResponse<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile[] files) {
        return SingleResponse.buildSuccess(qiniuService.uploadFile(files));
    }
}
