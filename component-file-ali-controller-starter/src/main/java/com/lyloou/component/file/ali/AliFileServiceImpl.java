package com.lyloou.component.file.ali;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
public class AliFileServiceImpl implements AliFileService {
    @Autowired
    private AliFileProperties aliFileProperties;

    @Autowired
    private OSSClient ossClient;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            3,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 5),
            new ThreadFactoryBuilder()
                    .setNamePrefix("file-ali-upload")
                    .build()
    );


    /**
     * [Java SDK快速入门](https://help.aliyun.com/document_detail/195870.html)
     */
    public String upload(String originFilename, byte[] content) {
        if (originFilename == null || content == null) {
            return null;
        }

        final TimeInterval timer = DateUtil.timer();
        try {
            log.info("start upload single file: {}", originFilename);
            String filePath = getNewFilename(originFilename);
            ossClient.putObject(aliFileProperties.getBucketName(), filePath, new ByteArrayInputStream(content));
            return aliFileProperties.getUrlPrefix() + "/" + filePath;
        } catch (Exception ex) {
            log.error("上传失败：", ex);
        } finally {
            log.info("end upload single file: {}, spend:{}ms", originFilename, timer.intervalMs());
        }
        return null;
    }

    private String getNewFilename(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtil.randomInt(100, 999) + "." +
                StrUtil.subAfter(sourceFileName, ".", true);
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
