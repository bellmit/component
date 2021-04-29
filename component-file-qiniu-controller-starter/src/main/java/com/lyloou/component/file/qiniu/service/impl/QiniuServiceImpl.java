package com.lyloou.component.file.qiniu.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.json.JSONUtil;
import com.lyloou.component.file.qiniu.config.QiniuProperties;
import com.lyloou.component.file.qiniu.service.QiniuService;
import com.qiniu.http.Response;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/4/29
 */
@Service
@Slf4j
public class QiniuServiceImpl implements QiniuService {
    @Autowired
    private QiniuProperties qiNiuUploadConfig;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            3,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 5),
            new ThreadFactoryBuilder()
                    .setNamePrefix("file-qiniu-upload")
                    .build()
    );


    /**
     * https://developer.qiniu.com/kodo/1239/java#upload-bytes
     *
     * @param file 文件
     * @return 结果
     */
    public String upload(MultipartFile file) {
        final String filename = file.getOriginalFilename();
        final TimeInterval timer = DateUtil.timer();
        log.info("start upload single file: {}", filename);

        //...生成上传凭证，然后准备上传
        String accessKey = qiNiuUploadConfig.getAccessKey();
        String secretKey = qiNiuUploadConfig.getSecretKey();
        String bucket = qiNiuUploadConfig.getBucket();
        String bucketUrl = qiNiuUploadConfig.getBucketUrl();
        String uploadDir = qiNiuUploadConfig.getUploadDir();

        String newFileName = getNewFilename(filename);

        //构造一个带指定 Region 对象的配置类
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String key = uploadDir + newFileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
            final String url = bucketUrl + "/" + putRet.key;
            return url;
        } catch (Exception ex) {
            log.error("上传失败：", ex);
        } finally {
            log.info("end upload single file: {}, spend:{}ms", filename, timer.intervalMs());
        }
        return null;
    }

    private String getNewFilename(String filename) {
        final String filePrefix = Objects.requireNonNull(filename).substring(0, filename.indexOf(".")).replace(" ", "_");
        final String fileExt = Objects.requireNonNull(filename).substring(filename.lastIndexOf(".")).toLowerCase();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return String.format("%s_%s%s%s",
                filePrefix,
                df.format(new Date()),
                String.format("%03d", new Random().nextInt(1000)),
                fileExt);
    }

    @Override
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile[] files) {
        final TimeInterval timer = DateUtil.timer();
        log.info("start upload file size: {}", files.length);
        Map<String, String> map = new LinkedHashMap<>();
        final List<CompletableFuture<Void>> futureList = Arrays.stream(files)
                .map(file -> CompletableFuture.runAsync(() ->
                        map.put(file.getOriginalFilename(), upload(file)), executor))
                .collect(Collectors.toList());

        //noinspection ResultOfMethodCallIgnored
        futureList.stream()
                .map(CompletableFuture::join)
                .count();
        log.info("end upload file size: {}, spend:{}ms", files.length, timer.intervalMs());
        return map;
    }
}
