package com.lyloou.component.dbmysql.support.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class IPUtils {
    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        final String unknown = "UNKNOWN";

        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String s : ips) {
                if (!(unknown.equalsIgnoreCase(s))) {
                    ip = s;
                    break;
                }
            }
        }

        // 根据网卡取本机配置的IP
        if (Stream.of(
                "127.0.0.1",
                "0:0:0:0:0:0:0:1")
                .collect(Collectors.toList())
                .contains(ip)) {

            //noinspection SSBasedInspection
            try {
                ip = getLocalIp();
            } catch (Exception e) {
                log.warn("get local ip error", e);
                ip = unknown;
            }
        }
        return ip;
    }


    /**
     * 获取本机IP
     */
    public static String getLocalIp() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }
}
