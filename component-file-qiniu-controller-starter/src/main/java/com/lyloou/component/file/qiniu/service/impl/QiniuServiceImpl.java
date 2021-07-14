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
import com.qiniu.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
     * @param originFilename
     * @param content
     * @return 结果
     */
    public String upload(String originFilename, byte[] content) {
        if (originFilename == null || content == null) {
            return null;
        }

        final TimeInterval timer = DateUtil.timer();
        log.info("start upload single file: {}", originFilename);

        //...生成上传凭证，然后准备上传
        String accessKey = qiNiuUploadConfig.getAccessKey();
        String secretKey = qiNiuUploadConfig.getSecretKey();
        String bucket = qiNiuUploadConfig.getBucket();
        String bucketUrl = qiNiuUploadConfig.getBucketUrl();
        String uploadDir = qiNiuUploadConfig.getUploadDir();

        String newFileName = getNewFilename(originFilename);

        //构造一个带指定 Region 对象的配置类
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String key = uploadDir + newFileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(content, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
            final String url = bucketUrl + "/" + putRet.key;
            return url;
        } catch (Exception ex) {
            log.error("上传失败：", ex);
        } finally {
            log.info("end upload single file: {}, spend:{}ms", originFilename, timer.intervalMs());
        }
        return null;
    }

    private String getNewFilename(String filename) {
        final String filePrefix = Objects.requireNonNull(filename).substring(0, filename.indexOf(".")).replace(" ", "_");
        final String fileExt = Objects.requireNonNull(filename).substring(filename.lastIndexOf(".")).toLowerCase();
        final String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        final String random = String.format("%03d", new Random().nextInt(1000));
        return String.format("%s_%s%s%s", filePrefix, date, random, fileExt);
    }

    @Override
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile[] files) {
        Map<String, String> map = new LinkedHashMap<>();
        if (files.length <= 0) {
            return map;
        }

        final TimeInterval timer = DateUtil.timer();
        Map<String, byte[]> nameToContent = new LinkedHashMap<>();
        Arrays.stream(files).forEach(it -> {
            try {
                // [上传文件异步处理注意事项 - SegmentFault 思否](https://segmentfault.com/a/1190000038544706)
                // [Spring async file upload and processing - Stack Overflow](https://stackoverflow.com/questions/36565597/spring-async-file-upload-and-processing)
                nameToContent.put(it.getOriginalFilename(), IOUtils.toByteArray(it.getInputStream()));
            } catch (Exception e) {
                log.warn("上传失败，IO异常", e);
                nameToContent.put(it.getOriginalFilename(), null);
            }
        });

        final List<CompletableFuture<Void>> futureList = nameToContent.keySet().stream()
                .map(name -> CompletableFuture.runAsync(() -> doUploadFile(map, name, nameToContent), executor))
                .collect(Collectors.toList());

        //noinspection ResultOfMethodCallIgnored
        futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.info("end upload file size: {}, spend:{}ms", files.length, timer.intervalMs());
        return map;
    }

    private void doUploadFile(Map<String, String> map, String name, Map<String, byte[]> nameToContent) {
        final String url = upload(name, nameToContent.get(name));
        map.put(name, url);
    }
}
