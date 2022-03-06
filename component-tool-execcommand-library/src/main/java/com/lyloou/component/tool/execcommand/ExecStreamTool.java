package com.lyloou.component.tool.execcommand;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.NullOutputStream;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.OutputStream;

/**
 * @author lilou
 * @date 2022/3/6 18:08
 */
public class ExecStreamTool {

    public static OutputStream getNullOutputStream() {
        return NullOutputStream.NULL_OUTPUT_STREAM;
    }

    public static OutputStream getFileOutputStream(String path) {
        return FileUtil.getOutputStream(path);
    }


    /**
     * 再通过 asXX 方法获取具体的 stream
     */
    public static Slf4jStream getSlf4jStream(String logName) {
        return Slf4jStream.of(LoggerFactory.getLogger(logName));
    }

}
