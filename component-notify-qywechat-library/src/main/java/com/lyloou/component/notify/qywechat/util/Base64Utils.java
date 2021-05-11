package com.lyloou.component.notify.qywechat.util;


import cn.hutool.core.util.StrUtil;
import com.lyloou.component.notify.qywechat.QyWechatException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;


/**
 * @author lilou
 */
public class Base64Utils {


    /**
     * 本地图片转换
     *
     * @param imgFile 图片本地路径
     * @return 结果
     */
    public static ImageBase64Md5 imageToBase64ByLocal(String imgFile) {

        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            throw new QyWechatException(e);
        }
        String base64 = Base64.getEncoder().encodeToString(data);
        return new ImageBase64Md5(base64, DigestUtils.md5Hex(data));
    }

    /**
     * 在线图片转换
     *
     * @param imgUrl 图片线上路径
     * @return
     */
    public static ImageBase64Md5 imageToBase64ByOnline(String imgUrl) {
        try {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            // 创建URL
            URL url = new URL(imgUrl);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            byte[] b = data.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(b);
            String md5 = DigestUtils.md5Hex(b);
            is.close();
            conn.disconnect();
            data.close();
            return new ImageBase64Md5(base64, md5);
        } catch (IOException e) {
            throw new QyWechatException(e);
        }
    }

    /**
     * base64字符串转换成图片
     *
     * @param imgStr      base64字符
     * @param imgFilePath 图片存放路径
     * @return
     */
    public static boolean base64ToImage(String imgStr, String imgFilePath) {
        if (StrUtil.isEmpty(imgStr)) {
            return false;
        }
        try {
            // Base64解码
            byte[] b = Base64.getDecoder().decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            throw new QyWechatException(e);
        }
    }

    public static void main(String[] args) throws Exception {

        // 在线图片地址
        String string = "https://t8.baidu.com/it/u=3775602298,2697846462&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1595834379&t=f2fc08916bdec3f7b3b8c0e018d04ae6";
        ImageBase64Md5 image = Base64Utils.imageToBase64ByOnline(string);
        System.out.println(image.getMd5());

        // 本地图片地址
        System.out.println(Base64Utils.base64ToImage(image.getBase64(), "D:/test2.jpg"));
    }

    public static class ImageBase64Md5 {

        private String base64;
        private String md5;

        public ImageBase64Md5(String base64, String md5) {
            this.base64 = base64;
            this.md5 = md5;
        }

        public String getBase64() {
            return base64;
        }

        public void setBase64(String base64) {
            this.base64 = base64;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

    }
}
