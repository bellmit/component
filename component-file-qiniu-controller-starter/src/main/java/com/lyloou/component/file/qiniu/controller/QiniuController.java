package com.lyloou.component.file.qiniu.controller;

import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.file.qiniu.service.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@Api(tags = "【component】文件-七牛上传接口")
public class QiniuController {

    @Autowired
    private QiniuService qiniuService;


    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件，返回键值对：文件名->URL地址",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SingleResponse<Map<String, String>> uploadFile(
            @ApiParam(value = "文件列表", required = true)
            @RequestParam("file") MultipartFile[] files) {
        return SingleResponse.buildSuccess(qiniuService.uploadFile(files));
    }
}
